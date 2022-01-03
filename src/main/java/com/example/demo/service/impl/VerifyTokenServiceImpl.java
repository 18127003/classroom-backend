package com.example.demo.service.impl;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.common.exception.InvalidVerifyTokenException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.entity.VerifyToken;
import com.example.demo.repository.VerifyTokenRepository;
import com.example.demo.service.VerifyTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class VerifyTokenServiceImpl implements VerifyTokenService {
    private final VerifyTokenRepository verifyTokenRepository;

    @Override
    public String createVerifyToken(Account account, VerifyTokenType type, Integer expireMinute) {
        var tokenString = UUID.randomUUID().toString();
        var calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.add(Calendar.MINUTE, expireMinute);
        var token = new VerifyToken(tokenString, Calendar.getInstance().getTime(), type, account);
        verifyTokenRepository.save(token);
        return tokenString;
    }

    @Override
    public VerifyToken verifyToken(String tokenString) {
        var token = verifyTokenRepository.getByToken(tokenString);
        if (token==null){
            throw new RTException(new RecordNotFoundException(tokenString, VerifyToken.class.getSimpleName()));
        }
        if (token.getExpiry().after(Calendar.getInstance().getTime())){
            throw new RTException(new InvalidVerifyTokenException(tokenString));
        }
        return token;
    }
}

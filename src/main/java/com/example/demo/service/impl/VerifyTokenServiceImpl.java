package com.example.demo.service.impl;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.common.exception.InvalidVerifyTokenException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.entity.VerifyToken;
import com.example.demo.repository.VerifyTokenRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.VerifyTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class VerifyTokenServiceImpl implements VerifyTokenService {
    private final VerifyTokenRepository verifyTokenRepository;
    private final AccountService accountService;

    @Override
    public VerifyToken createVerifyToken(Account account, VerifyTokenType type, Integer expireMinute) {
        var tokenString = UUID.randomUUID().toString();
        var calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.add(Calendar.MINUTE, expireMinute);
        var token = new VerifyToken(tokenString, calendar.getTime(), type, account);
        return verifyTokenRepository.save(token);
    }

    @Override
    public VerifyToken verifyToken(String tokenString) {
        var token = getByTokenString(tokenString);
        var accountId = token.getAccount().getId();
        if (accountService.checkLocked(accountId)){
            throw new RTException(new RecordNotFoundException(accountId.toString(), Account.class.getSimpleName()));
        }
        if (token.getExpiry().before(Calendar.getInstance().getTime())){
            throw new RTException(new InvalidVerifyTokenException(tokenString));
        }
        return token;
    }

    @Override
    public VerifyToken rotateVerifyToken(VerifyToken token, Integer expireMinute) {
        token.setToken(UUID.randomUUID().toString());
        var calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.add(Calendar.MINUTE, expireMinute);
        token.setExpiry(calendar.getTime());
        return verifyTokenRepository.save(token);
    }

    @Override
    public VerifyToken getByTokenString(String tokenString) {
        var token = verifyTokenRepository.getByToken(tokenString);
        if (token==null){
            throw new RTException(new RecordNotFoundException(tokenString, VerifyToken.class.getSimpleName()));
        }
        return token;
    }

    @Override
    public VerifyToken getOrCreateToken(Account account, VerifyTokenType type, Integer expireMinute) {
        var existedToken = verifyTokenRepository.getByAccount(account.getId(), type);
        // No record
        if(existedToken == null){
            return createVerifyToken(account, type, expireMinute);
        }
        // Still valid token
        if(existedToken.getExpiry().after(Date.from(Instant.now()))){
            return existedToken;
        }
        // Reset expiry
        var calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));
        calendar.add(Calendar.MINUTE, expireMinute);
        existedToken.setExpiry(calendar.getTime());
        return verifyTokenRepository.save(existedToken);
    }
}

package com.example.demo.service.impl;

import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account createAccount(Account account) {
        var existedAccount = accountRepository.findByName(account.getName());
        if(existedAccount!=null){
            throw new RTException(new DuplicateRecordException(existedAccount.getId().toString(),Account.class.getSimpleName()));
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(()->new RTException(new RecordNotFoundException(accountId.toString(), Account.class.getSimpleName())));
    }
}

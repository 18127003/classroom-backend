package com.example.demo.service.impl;

import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AccountService;
import com.example.demo.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordUtil passwordUtil;

    @Override
    public Account createAccount(Account account) {
        var existedAccount = accountRepository.findByEmail(account.getEmail());
        if(existedAccount!=null){
            throw new RTException(new DuplicateRecordException(existedAccount.getId().toString(),Account.class.getSimpleName()));
        }
        account.setPassword(passwordUtil.encodePassword(account.getPassword()));
        account.setName(account.getFirstName()+" "+account.getLastName());
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long id, Account update) {
        var account = accountRepository.findById(id)
                .orElseThrow(()->new RTException(new RecordNotFoundException(id.toString(), Account.class.getSimpleName())));
        if (!update.getEmail().equals(account.getEmail())){
            var email = accountRepository.findByEmail(update.getEmail());
            if(email!=null){
                throw new RTException(new DuplicateRecordException(email.getId().toString(), Account.class.getSimpleName()));
            }
        }

        var studentId = update.getStudentId();
        if(studentId!=null && !studentId.equals(account.getStudentId())){
            if(accountRepository.findByStudentId(studentId)!=null){
                throw new RTException(new DuplicateRecordException(studentId, Account.class.getSimpleName()));
            }
        }

        account.setFirstName(update.getFirstName());
        account.setLastName(update.getLastName());
        account.setName(update.getFirstName()+" "+update.getLastName());
        account.setEmail(update.getEmail());
        account.setStudentId(studentId);

        return accountRepository.save(account);
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        var account = accountRepository.findById(id)
                .orElseThrow(()->new RTException(new RecordNotFoundException(id.toString(), Account.class.getSimpleName())));
        if(passwordUtil.checkPassword(oldPassword, account.getPassword())){
            account.setPassword(passwordUtil.encodePassword(newPassword));
            accountRepository.save(account);
            return true;
        }
        return false;
    }
}

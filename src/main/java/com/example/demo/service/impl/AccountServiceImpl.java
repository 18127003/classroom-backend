package com.example.demo.service.impl;

import com.example.demo.common.enums.AccountStatus;
import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.InvalidVerifyTokenException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.LockedAccountRepository;
import com.example.demo.repository.StudentInfoRepository;
import com.example.demo.repository.VerifyTokenRepository;
import com.example.demo.service.AccountService;
import com.example.demo.service.VerifyTokenService;
import com.example.demo.util.EmailSender;
import com.example.demo.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static com.example.demo.common.constant.Constants.HOST_EMAIL;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final VerifyTokenService verifyTokenService;
    private final LockedAccountRepository lockedAccountRepository;
    private final EmailSender emailSender;
    private final PasswordUtil passwordUtil;

    @Override
    public Account getAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(()->new RTException(new RecordNotFoundException(accountId.toString(),Account.class.getSimpleName())));
    }

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
    public Account updateAccount(UUID id, Account update) {
        var account = getAccountById(id);
        if (!update.getEmail().equals(account.getEmail())){
            var email = accountRepository.findByEmail(update.getEmail());
            if(email!=null){
                throw new RTException(new DuplicateRecordException(email.getId().toString(), Account.class.getSimpleName()));
            }
        }

//        var studentId = update.getStudentId();
//        if(studentId!=null && !studentId.equals(account.getStudentId())){
//            if(accountRepository.findByStudentId(studentId)!=null){
//                throw new RTException(new DuplicateRecordException(studentId, Account.class.getSimpleName()));
//            }
//        }

        account.setFirstName(update.getFirstName());
        account.setLastName(update.getLastName());
        account.setName(update.getFirstName()+" "+update.getLastName());
        account.setEmail(update.getEmail());
//        account.setStudentId(studentId);

        return accountRepository.save(account);
    }

    @Override
    public boolean changePassword(UUID id, String oldPassword, String newPassword) {
        var account = getAccountById(id);
        if(passwordUtil.checkPassword(oldPassword, account.getPassword())){
            account.setPassword(passwordUtil.encodePassword(newPassword));
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public void updateStudentId(UUID accountId, String studentId, String name) {
        var account = getAccountById(accountId);
        if (account==null){
            throw new RTException(new RecordNotFoundException(accountId.toString(), Account.class.getSimpleName()));
        }
        // detach previous linked student info
        var existedStudentInfo = account.getStudentInfo();
        if (existedStudentInfo!=null){
            existedStudentInfo.setClassroomAccount(null);
        }

        // find existed student info
        var info = studentInfoRepository.findByStudentId(studentId);
        if (info != null){
            if(info.getClassroomAccount()!=null){
                throw new RTException(new DuplicateRecordException(studentId, StudentInfo.class.getSimpleName()));
            } else {
                info.setClassroomAccount(account);
            }
        } else {
            info = new StudentInfo(studentId, name);
            info.setClassroomAccount(account);
        }
        studentInfoRepository.save(info);
    }

    @Override
    public void sendResetPasswordEmail(String frontPath, Account account) throws IOException {
        // create token
        var token = verifyTokenService.createVerifyToken(account, VerifyTokenType.PASSWORD_RESET, 15);

        // send email
        var subject = "Reset password link for Classroom";
        var content = "Please click the link below to reset your password: "+frontPath+"?token="+token;
        emailSender.sendEmail(HOST_EMAIL, account.getEmail(), subject, content);
    }

    @Override
    public Account getAccountByEmail(String email) {
        var account = accountRepository.findByEmail(email);
        if(account == null){
            throw new RTException(new RecordNotFoundException(email, Account.class.getSimpleName()));
        }
        return account;
    }

    @Override
    public void resetPassword(String tokenString, String password) {
        var token = verifyTokenService.verifyToken(tokenString);
        // set new password
        var account = token.getAccount();
        account.setPassword(passwordUtil.encodePassword(password));
        accountRepository.save(account);
    }

    @Override
    public void sendAccountActivateEmail(String frontPath, Account account) throws IOException {
        // create token
        var token = verifyTokenService.createVerifyToken(account, VerifyTokenType.ACCOUNT_ACTIVATE, 60);

        // send email
        var subject = "Activate account link for Classroom Account "+account.getName();
        var content = "Please click the link below to activate your account: "+frontPath+"?token="+token;
        emailSender.sendEmail(HOST_EMAIL, account.getEmail(), subject, content);
    }

    @Override
    public void activateAccount(String tokenString) {
        var token = verifyTokenService.verifyToken(tokenString);
        var account = token.getAccount();
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public boolean checkLocked(UUID accountId) {
        var lockedAccount = lockedAccountRepository.getByAccountId(accountId);
        return lockedAccount != null;
    }

    @Override
    public void lockAccount(UUID accountId) {
        var account = getAccountById(accountId);
        var lockedAccount = new LockedAccount(account);
        lockedAccountRepository.save(lockedAccount);
    }

    @Override
    public void unlockAccount(UUID accountId) {
        var locked = lockedAccountRepository.getByAccountId(accountId);
        if (locked==null){
            throw new RTException(new RecordNotFoundException(accountId.toString(), LockedAccount.class.getSimpleName()));
        }
        lockedAccountRepository.delete(locked);
    }
}

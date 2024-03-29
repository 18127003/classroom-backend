package com.example.demo.service.impl;

import com.example.demo.common.enums.AccountStatus;
import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.common.exception.DuplicateRecordException;
import com.example.demo.common.exception.InvalidVerifyTokenException;
import com.example.demo.common.exception.RTException;
import com.example.demo.common.exception.RecordNotFoundException;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.AccountService;
import com.example.demo.service.VerifyTokenService;
import com.example.demo.util.EmailSender;
import com.example.demo.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

import static com.example.demo.common.constant.Constants.HOST_EMAIL;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final StudentInfoRepository studentInfoRepository;
    private final LockedAccountRepository lockedAccountRepository;
    private final ReceiverRepository receiverRepository;
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
        account.setStatus(AccountStatus.CREATED);
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

        account.setFirstName(update.getFirstName());
        account.setLastName(update.getLastName());
        account.setName(update.getFirstName()+" "+update.getLastName());
        account.setEmail(update.getEmail());

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
    public void removeStudentId(UUID accountId) {
        var account = getAccountById(accountId);
        var studentInfo = account.getStudentInfo();
        if(studentInfo != null){
            studentInfo.setClassroomAccount(null);
            studentInfoRepository.save(studentInfo);
        }
    }

    @Override
    public void sendResetPasswordEmail(String frontPath, Account account, VerifyToken token) throws IOException {
        // send email
        var subject = "Reset password link for Classroom";
        var content = "Please click the link below to reset your password: "+frontPath+"?token="+token.getToken();
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
    public void resetPassword(VerifyToken token, String password) {
        // set new password
        var account = token.getAccount();
        account.setPassword(passwordUtil.encodePassword(password));
        accountRepository.save(account);
    }

    @Override
    public void sendAccountActivateEmail(String frontPath, Account account, VerifyToken token) throws IOException {
        // send email
        var subject = "Activate account link for Classroom Account "+account.getName();
        var content = "Please click the link below to activate your account: "+frontPath+"?token="+token.getToken();
        emailSender.sendEmail(HOST_EMAIL, account.getEmail(), subject, content);
    }

    @Override
    public void activateAccount(VerifyToken token) {
        var account = token.getAccount();
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccount(boolean isDesc, String q) {
        if (StringUtils.isEmpty(q)){
            return accountRepository.getAllNonLockedAccount(isDesc);
        }
        return accountRepository.getAllNonLockedAccountSearch(isDesc, q);
    }

    @Override
    public List<Account> getAllLockedAccount(boolean isDesc, String q) {
        if (StringUtils.isEmpty(q)){
            return lockedAccountRepository.getAllLockedSort(isDesc);
        }
        return lockedAccountRepository.getAllLockedSortSearch(isDesc, q);
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

    @Override
    public List<Notification> getAllNotification(UUID accountId) {
        return receiverRepository.getAllOfAccount(accountId);
    }
}

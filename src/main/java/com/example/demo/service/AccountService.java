package com.example.demo.service;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.entity.Account;
import com.example.demo.entity.VerifyToken;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    Account getAccountById(UUID accountId);

    Account createAccount(Account account);

    Account updateAccount(UUID id, Account update);

    boolean changePassword(UUID id, String oldPassword, String newPassword);

    void updateStudentId(UUID accountId, String studentId, String name);

    void removeStudentId(UUID accountId);

    void sendResetPasswordEmail(String frontPath, Account account, VerifyToken token) throws IOException;

    Account getAccountByEmail(String email);

    void resetPassword(VerifyToken token, String password);

    void sendAccountActivateEmail(String frontPath, Account account, VerifyToken token) throws IOException;

    void activateAccount(VerifyToken token);

    List<Account> getAllAccount();

    boolean checkLocked(UUID accountId);

    void lockAccount(UUID accountId);

    void unlockAccount(UUID accountId);
}

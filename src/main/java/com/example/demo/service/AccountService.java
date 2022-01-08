package com.example.demo.service;

import com.example.demo.entity.Account;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    Account getAccountById(UUID accountId);

    Account createAccount(Account account);

    Account updateAccount(UUID id, Account update);

    boolean changePassword(UUID id, String oldPassword, String newPassword);

    void updateStudentId(UUID accountId, String studentId, String name);

    void sendResetPasswordEmail(String frontPath, Account account) throws IOException;

    Account getAccountByEmail(String email);

    void resetPassword(String tokenString, String password);

    void sendAccountActivateEmail(String frontPath, Account account) throws IOException;

    void activateAccount(String tokenString);

    List<Account> getAllAccount();

    boolean checkLocked(UUID accountId);

    void lockAccount(UUID accountId);

    void unlockAccount(UUID accountId);
}

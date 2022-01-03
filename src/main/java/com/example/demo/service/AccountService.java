package com.example.demo.service;

import com.example.demo.entity.Account;

import java.io.IOException;

public interface AccountService {
    Account getAccountById(Long accountId);

    Account createAccount(Account account);

    Account updateAccount(Long id, Account update);

    boolean changePassword(Long id, String oldPassword, String newPassword);

    void updateStudentId(Long accountId, String studentId, String name);

    void sendResetPasswordEmail(String frontPath, Account account) throws IOException;

    Account getAccountByEmail(String email);

    void resetPassword(String tokenString, String password);

    void sendAccountActivateEmail(String frontPath, Account account) throws IOException;

    void activateAccount(String tokenString);
}

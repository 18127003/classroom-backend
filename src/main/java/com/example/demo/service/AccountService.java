package com.example.demo.service;

import com.example.demo.entity.Account;

public interface AccountService {
    Account createAccount(Account account);

    Account updateAccount(Long id, Account update);

    boolean changePassword(Long id, String oldPassword, String newPassword);
}

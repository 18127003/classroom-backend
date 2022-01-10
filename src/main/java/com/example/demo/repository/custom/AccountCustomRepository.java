package com.example.demo.repository.custom;

import com.example.demo.entity.Account;

import java.util.List;

public interface AccountCustomRepository {
    // deprecated
    Account findByName(String name);

    Account findByEmail(String email);

    List<Account> getAllNonLockedAccount(boolean isDesc);

    List<Account> getAllNonLockedAccountSearch(boolean isDesc, String q);
}

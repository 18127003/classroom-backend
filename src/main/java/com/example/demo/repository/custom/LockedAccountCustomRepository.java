package com.example.demo.repository.custom;

import com.example.demo.entity.Account;
import com.example.demo.entity.LockedAccount;

import java.util.List;
import java.util.UUID;

public interface LockedAccountCustomRepository {
    LockedAccount getByAccountId(UUID accountId);

    List<Account> getAllLockedSort(boolean isDesc);

    List<Account> getAllLockedSortSearch(boolean isDesc, String q);
}

package com.example.demo.repository.custom;

import com.example.demo.entity.LockedAccount;

public interface LockedAccountCustomRepository {
    LockedAccount getByAccountId(Long accountId);
}

package com.example.demo.repository.custom;

import com.example.demo.entity.LockedAccount;

import java.util.UUID;

public interface LockedAccountCustomRepository {
    LockedAccount getByAccountId(UUID accountId);
}

package com.example.demo.repository.impl;

import com.example.demo.entity.LockedAccount;
import com.example.demo.entity.QLockedAccount;
import com.example.demo.repository.custom.LockedAccountCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LockedAccountRepositoryImpl extends AbstractRepositoryImpl<LockedAccount> implements LockedAccountCustomRepository {
    @Override
    public LockedAccount getByAccountId(UUID accountId) {
        return selectFrom(QLockedAccount.lockedAccount).where(QLockedAccount.lockedAccount.account.id.eq(accountId))
                .fetchOne();
    }
}

package com.example.demo.repository.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.QAccount;
import com.example.demo.entity.QLockedAccount;
import com.example.demo.repository.custom.AccountCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountRepositoryImpl extends AbstractRepositoryImpl<Account> implements AccountCustomRepository {
    @Override
    public Account findByName(String name) {
        return selectFrom(QAccount.account).where(QAccount.account.name.eq(name)).fetchOne();
    }

    @Override
    public Account findByEmail(String email) {
        return selectFrom(QAccount.account).where(QAccount.account.email.eq(email)).fetchOne();
    }

    @Override
    public List<Account> getAllNonLockedAccount() {
        return selectFrom(QAccount.account)
                .leftJoin(QLockedAccount.lockedAccount)
                .on(QAccount.account.id.eq(QLockedAccount.lockedAccount.account.id))
                .where(QLockedAccount.lockedAccount.id.isNull())
                .fetch();
    }
}

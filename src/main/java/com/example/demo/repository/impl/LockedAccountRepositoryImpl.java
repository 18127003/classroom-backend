package com.example.demo.repository.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.LockedAccount;
import com.example.demo.entity.QAccount;
import com.example.demo.entity.QLockedAccount;
import com.example.demo.repository.custom.LockedAccountCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class LockedAccountRepositoryImpl extends AbstractRepositoryImpl<LockedAccount> implements LockedAccountCustomRepository {
    @Override
    public LockedAccount getByAccountId(UUID accountId) {
        return selectFrom(QLockedAccount.lockedAccount).where(QLockedAccount.lockedAccount.account.id.eq(accountId))
                .fetchOne();
    }

    @Override
    public List<Account> getAllLockedSort(boolean isDesc) {
        var res = selectFrom(QLockedAccount.lockedAccount)
                .join(QAccount.account)
                .on(QLockedAccount.lockedAccount.account.id.eq(QAccount.account.id))
                .select(QAccount.account);
        if(isDesc){
            return res.orderBy(QAccount.account.createdAt.desc()).fetch();
        }
        return res.orderBy(QAccount.account.createdAt.asc()).fetch();
    }

    @Override
    public List<Account> getAllLockedSortSearch(boolean isDesc, String q) {
        var res = selectFrom(QLockedAccount.lockedAccount)
                .join(QAccount.account)
                .on(QLockedAccount.lockedAccount.account.id.eq(QAccount.account.id))
                .select(QAccount.account)
                .where(QAccount.account.name.contains(q)
                        .or(QAccount.account.email.contains(q)));
        if(isDesc){
            return res.orderBy(QAccount.account.createdAt.desc()).fetch();
        }
        return res.orderBy(QAccount.account.createdAt.asc()).fetch();
    }
}

package com.example.demo.repository.impl;

import com.example.demo.common.enums.AccountRole;
import com.example.demo.entity.Account;
import com.example.demo.entity.QAccount;
import com.example.demo.repository.AccountCustomRepository;
import org.springframework.stereotype.Repository;

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
    public AccountRole getAccountRole(Long id) {
        return selectFrom(QAccount.account)
                .where(QAccount.account.id.eq(id))
                .select(QAccount.account.accountRole)
                .fetchOne();
    }
}

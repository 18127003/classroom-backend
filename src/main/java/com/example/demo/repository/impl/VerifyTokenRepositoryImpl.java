package com.example.demo.repository.impl;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.entity.QVerifyToken;
import com.example.demo.entity.VerifyToken;
import com.example.demo.repository.custom.VerifyTokenCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class VerifyTokenRepositoryImpl extends AbstractRepositoryImpl<VerifyToken> implements VerifyTokenCustomRepository {
    @Override
    public VerifyToken getByToken(String token) {
        return selectFrom(QVerifyToken.verifyToken).where(QVerifyToken.verifyToken.token.eq(token)).fetchOne();
    }

    @Override
    public VerifyToken getByAccount(UUID accountId, VerifyTokenType type) {
        return selectFrom(QVerifyToken.verifyToken)
                .where(QVerifyToken.verifyToken.account.id.eq(accountId)
                    .and(QVerifyToken.verifyToken.tokenType.eq(type)))
                .fetchOne();
    }
}

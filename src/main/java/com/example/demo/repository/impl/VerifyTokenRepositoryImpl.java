package com.example.demo.repository.impl;

import com.example.demo.entity.QVerifyToken;
import com.example.demo.entity.VerifyToken;
import com.example.demo.repository.custom.VerifyTokenCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class VerifyTokenRepositoryImpl extends AbstractRepositoryImpl<VerifyToken> implements VerifyTokenCustomRepository {
    @Override
    public VerifyToken getByToken(String token) {
        return selectFrom(QVerifyToken.verifyToken).where(QVerifyToken.verifyToken.token.eq(token)).fetchOne();
    }
}

package com.example.demo.service;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.entity.Account;
import com.example.demo.entity.VerifyToken;

public interface VerifyTokenService {
    VerifyToken createVerifyToken(Account account, VerifyTokenType type, Integer expireMinute);

    VerifyToken verifyToken(String tokenString);

    VerifyToken rotateVerifyToken(VerifyToken token, Integer expireMinute);

    VerifyToken getByTokenString(String tokenString);

    VerifyToken getOrCreateToken(Account account, VerifyTokenType type, Integer expireMinute);
}

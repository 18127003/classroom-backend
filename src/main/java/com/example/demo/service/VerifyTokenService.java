package com.example.demo.service;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.entity.Account;
import com.example.demo.entity.VerifyToken;

public interface VerifyTokenService {
    String createVerifyToken(Account account, VerifyTokenType type, Integer expireMinute);

    VerifyToken verifyToken(String tokenString);
}

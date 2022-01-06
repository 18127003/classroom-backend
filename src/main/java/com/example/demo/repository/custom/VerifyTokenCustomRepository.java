package com.example.demo.repository.custom;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.entity.VerifyToken;

public interface VerifyTokenCustomRepository {
    VerifyToken getByToken(String token);

    VerifyToken getByAccount(Long accountId, VerifyTokenType type);
}

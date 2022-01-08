package com.example.demo.repository.custom;

import com.example.demo.common.enums.VerifyTokenType;
import com.example.demo.entity.VerifyToken;

import java.util.UUID;

public interface VerifyTokenCustomRepository {
    VerifyToken getByToken(String token);

    VerifyToken getByAccount(UUID accountId, VerifyTokenType type);
}

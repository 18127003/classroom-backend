package com.example.demo.repository.custom;

import com.example.demo.entity.VerifyToken;

public interface VerifyTokenCustomRepository {
    VerifyToken getByToken(String token);
}

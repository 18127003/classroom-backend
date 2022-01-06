package com.example.demo.dto.jwt;

import com.example.demo.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInResponse {
    private AccountDto account;
    private String refreshToken;
}

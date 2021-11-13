package com.example.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PasswordRequestDto {
    private String oldPassword;
    private String newPassword;
}

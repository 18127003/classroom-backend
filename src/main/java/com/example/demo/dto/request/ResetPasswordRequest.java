package com.example.demo.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String password;
    String token;
}

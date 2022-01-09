package com.example.demo.dto;

import com.example.demo.common.enums.AccountStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class AdminDto {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private AccountStatus status;
}

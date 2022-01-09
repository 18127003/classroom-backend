package com.example.demo.dto;

import com.example.demo.common.enums.AccountStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountDto {
    private String name;
    private String firstName;
    private String lastName;
    private String studentId;
    private UUID id;
    private String email;
    private AccountStatus status;
}

package com.example.demo.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AdminDto {
    private UUID id;
    private String name;
    private String email;
    private String role;
}

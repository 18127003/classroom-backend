package com.example.demo.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ParticipantDto {
    private UUID accountId;
    private String name;
    private String studentId;
    private String email;
    private String role;
    private Boolean hidden;
}

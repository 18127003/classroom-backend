package com.example.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


/***
 * Not exposed
 */
@Data
@RequiredArgsConstructor
public class StudentInfoDto {
    private Long id;
    private String studentId;
    private String name;
    private String accountMail;
}

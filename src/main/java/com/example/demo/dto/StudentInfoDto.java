package com.example.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class StudentInfoDto {
    private Long id;
    private String studentId;
    private String name;
    private List<SubmissionDto> submissions;
}

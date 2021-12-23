package com.example.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class StudentInfoClassroomDto {
    private Long id;
    private String studentId;
    private String name;
    private String accountMail;
    private List<SubmissionDto> submissions;
}

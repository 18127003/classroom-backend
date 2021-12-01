package com.example.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SubmissionDto {
    private Long id;
    private Long assignmentId;
    private String studentId;
    private Long classroomId;
    private Integer grade;
    private Integer maxGrade;
}

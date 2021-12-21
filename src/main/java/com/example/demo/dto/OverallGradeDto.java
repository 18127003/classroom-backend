package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OverallGradeDto {
    private Integer overallGrade;
    private Integer maxGrade;
}

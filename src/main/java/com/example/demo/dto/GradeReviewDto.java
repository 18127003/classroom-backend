package com.example.demo.dto;

import lombok.Data;

@Data
public class GradeReviewDto {
    private Long id;
    private Integer expectGrade;
    private String explanation;
    private Integer currentGrade;
    private String assignment;
    private String status;
    private String author;
}

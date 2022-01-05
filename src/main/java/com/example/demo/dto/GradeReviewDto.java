package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class GradeReviewDto {
    private Long id;
    private Integer expectGrade;
    private String explanation;
    private Integer currentGrade;
    private String assignment;
    private Long assignmentId;
    private String status;
    private String author;
    private List<CommentDto> comments;
}

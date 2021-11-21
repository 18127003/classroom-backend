package com.example.demo.dto;

import lombok.Data;

@Data
public class AssignmentDto {
    private Long id;
    private String name;
    private String description;
    private Integer points;
    private String deadline;
    private String createdAt;
    private String creator;
    private String classroom; //optional
}

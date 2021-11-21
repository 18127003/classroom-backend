package com.example.demo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AssignmentDto {
    private Long id;
    private String name;
    private String description;
    private Integer points;
    private Date deadline;
    private Date createdAt;
    private String creator;
    private String classroom; //optional
}

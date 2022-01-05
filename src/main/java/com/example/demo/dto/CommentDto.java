package com.example.demo.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String by;
    private Long reviewId;
}

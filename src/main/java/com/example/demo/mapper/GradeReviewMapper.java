package com.example.demo.mapper;

import com.example.demo.dto.GradeReviewDto;
import com.example.demo.entity.GradeReview;
import com.example.demo.mapper.decorator.GradeReviewDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
@DecoratedWith(GradeReviewDecorator.class)
public interface GradeReviewMapper {
    GradeReviewDto toGradeReviewDto(GradeReview gradeReview);
}

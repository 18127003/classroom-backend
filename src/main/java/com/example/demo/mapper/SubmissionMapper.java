package com.example.demo.mapper;

import com.example.demo.dto.SubmissionDto;
import com.example.demo.entity.Submission;
import com.example.demo.mapper.decorator.SubmissionDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(SubmissionDecorator.class)
public interface SubmissionMapper {

    SubmissionDto toSubmissionDto(Submission submission);
}

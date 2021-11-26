package com.example.demo.mapper;

import com.example.demo.dto.StudentInfoDto;
import com.example.demo.entity.StudentInfo;
import com.example.demo.mapper.decorator.StudentInfoDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SubmissionMapper.class)
@DecoratedWith(StudentInfoDecorator.class)
public interface StudentInfoMapper {

    StudentInfoDto toStudentInfoDto(StudentInfo studentInfo);
}

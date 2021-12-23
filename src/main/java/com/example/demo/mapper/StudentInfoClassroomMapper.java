package com.example.demo.mapper;

import com.example.demo.dto.StudentInfoClassroomDto;
import com.example.demo.entity.StudentInfoClassroom;
import com.example.demo.mapper.decorator.StudentInfoClassroomDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SubmissionMapper.class)
@DecoratedWith(StudentInfoClassroomDecorator.class)
public interface StudentInfoClassroomMapper {
    StudentInfoClassroomDto toStudentInfoClassroomDto(StudentInfoClassroom studentInfo);
}

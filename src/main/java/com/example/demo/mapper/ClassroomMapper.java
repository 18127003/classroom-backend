package com.example.demo.mapper;

import com.example.demo.dto.ClassroomDto;
import com.example.demo.entity.Classroom;
import com.example.demo.mapper.decorator.ClassroomDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
@DecoratedWith(ClassroomDecorator.class)
public interface ClassroomMapper {

    ClassroomDto toClassroomDto(Classroom attachment);
}

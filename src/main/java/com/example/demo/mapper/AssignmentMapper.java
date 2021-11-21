package com.example.demo.mapper;

import com.example.demo.dto.AssignmentDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Assignment;
import com.example.demo.entity.Classroom;
import com.example.demo.mapper.decorator.AssignmentDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(AssignmentDecorator.class)
public interface AssignmentMapper {

    AssignmentDto toAssignmentDto(Assignment assignment);

    default String mapCreator(Account account){
        return account.getName();
    }

    default String mapClassroom(Classroom classroom) {return classroom.getName();}
}

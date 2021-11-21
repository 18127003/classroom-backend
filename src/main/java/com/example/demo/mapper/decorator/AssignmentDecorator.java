package com.example.demo.mapper.decorator;

import com.example.demo.dto.AssignmentDto;
import com.example.demo.entity.Assignment;
import com.example.demo.mapper.AssignmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AssignmentDecorator implements AssignmentMapper {

    @Autowired
    @Qualifier("delegate")
    private AssignmentMapper delegate;

    @Override
    public AssignmentDto toAssignmentDto(Assignment assignment) {
        return delegate.toAssignmentDto(assignment);
    }
}

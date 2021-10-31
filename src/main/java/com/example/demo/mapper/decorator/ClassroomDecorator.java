package com.example.demo.mapper.decorator;

import com.example.demo.dto.ClassroomDto;
import com.example.demo.entity.Classroom;
import com.example.demo.mapper.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ClassroomDecorator implements ClassroomMapper {
    @Autowired
    @Qualifier("delegate")
    private ClassroomMapper delegate;

    @Override
    public ClassroomDto toClassroomDto(Classroom attachment) {
        return delegate.toClassroomDto(attachment);
    }
}

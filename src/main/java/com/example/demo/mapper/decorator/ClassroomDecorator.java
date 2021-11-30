package com.example.demo.mapper.decorator;

import com.example.demo.dto.ClassroomDto;
import com.example.demo.entity.Classroom;
import com.example.demo.entity.Participant;
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

    @Override
    public ClassroomDto toAssignedClassroomDto(Participant participant) {
        var classroom = delegate.toClassroomDto(participant.getClassroom());
        classroom.setRole(participant.getRole().name());
        classroom.setCreator(participant.getClassroom().getCreator().getName());
        classroom.setStudentId(participant.getStudentId());
        return classroom;
    }
}

package com.example.demo.service.impl;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Override
    public List<Classroom> getClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom createClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }
}

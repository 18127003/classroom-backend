package com.example.demo.repository.impl;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClassroomRepositoryImpl extends AbstractRepositoryImpl<Classroom> implements ClassroomCustomRepository {
    @Override
    public List<Classroom> getClasses() {
        return null;
    }

    @Override
    public Classroom addClass() {
        return null;
    }
}

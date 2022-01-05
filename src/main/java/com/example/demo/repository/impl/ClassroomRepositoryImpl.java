package com.example.demo.repository.impl;

import com.example.demo.entity.Classroom;
import com.example.demo.entity.QClassroom;
import com.example.demo.repository.custom.ClassroomCustomRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ClassroomRepositoryImpl extends AbstractRepositoryImpl<Classroom> implements ClassroomCustomRepository {

    @Override
    public Classroom findByCode(String code) {
        return selectFrom(QClassroom.classroom).where(QClassroom.classroom.code.eq(code)).fetchOne();
    }
}

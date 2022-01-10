package com.example.demo.repository.impl;

import com.example.demo.entity.Classroom;
import com.example.demo.entity.QClassroom;
import com.example.demo.repository.custom.ClassroomCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClassroomRepositoryImpl extends AbstractRepositoryImpl<Classroom> implements ClassroomCustomRepository {

    @Override
    public Classroom findByCode(String code) {
        return selectFrom(QClassroom.classroom).where(QClassroom.classroom.code.eq(code)).fetchOne();
    }

    @Override
    public List<Classroom> findAllSort(boolean isDesc) {
        var res = selectFrom(QClassroom.classroom);
        if (isDesc){
            return res.orderBy(QClassroom.classroom.createdAt.desc()).fetch();
        }
        return res.orderBy(QClassroom.classroom.createdAt.asc()).fetch();
    }

    @Override
    public List<Classroom> findAllSortSearch(boolean isDesc, String q) {
        var res = selectFrom(QClassroom.classroom).where(QClassroom.classroom.name.contains(q));
        if (isDesc){
            return res.orderBy(QClassroom.classroom.createdAt.desc()).fetch();
        }
        return res.orderBy(QClassroom.classroom.createdAt.asc()).fetch();
    }
}

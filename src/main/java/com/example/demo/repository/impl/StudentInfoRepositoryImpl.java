package com.example.demo.repository.impl;

import com.example.demo.entity.QStudentInfo;
import com.example.demo.entity.StudentInfo;
import com.example.demo.repository.StudentInfoCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class StudentInfoRepositoryImpl extends AbstractRepositoryImpl<StudentInfo> implements StudentInfoCustomRepository {
    @Override
    public List<StudentInfo> findByListStudentId(Collection<String> studentIds) {
        return selectFrom(QStudentInfo.studentInfo)
                .where(QStudentInfo.studentInfo.studentId.in(studentIds))
                .fetch();
    }

    @Override
    public StudentInfo findByStudentId(String studentId) {
        return selectFrom(QStudentInfo.studentInfo)
                .where(QStudentInfo.studentInfo.studentId.eq(studentId))
                .fetchOne();
    }
}

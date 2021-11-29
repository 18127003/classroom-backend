package com.example.demo.repository.impl;

import com.example.demo.entity.QStudentInfo;
import com.example.demo.entity.StudentInfo;
import com.example.demo.repository.StudentInfoCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentInfoRepositoryImpl extends AbstractRepositoryImpl<StudentInfo> implements StudentInfoCustomRepository {
    @Override
    public StudentInfo findByStudentId(String studentId, Long classroomId) {
        return selectFrom(QStudentInfo.studentInfo)
                .where(QStudentInfo.studentInfo.studentId.eq(studentId)
                        .and(QStudentInfo.studentInfo.classroom.id.eq(classroomId)))
                .fetchOne();
    }

    @Override
    public List<StudentInfo> findAllStudentInfo(Long classroomId) {
        return selectFrom(QStudentInfo.studentInfo).where(QStudentInfo.studentInfo.classroom.id.eq(classroomId)).fetch();
    }
}

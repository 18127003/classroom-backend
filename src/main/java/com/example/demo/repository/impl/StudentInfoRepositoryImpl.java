package com.example.demo.repository.impl;

import com.example.demo.entity.QAssignment;
import com.example.demo.entity.QStudentInfo;
import com.example.demo.entity.QSubmission;
import com.example.demo.entity.StudentInfo;
import com.example.demo.repository.StudentInfoCustomRepository;
import com.querydsl.core.Tuple;
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

    @Override
    public List<Tuple> getExcelData(Long classId) {
        return selectFrom(QStudentInfo.studentInfo)
                .join(QAssignment.assignment)
                .on(QStudentInfo.studentInfo.classroom.id.eq(QAssignment.assignment.classroom.id))
                .leftJoin(QSubmission.submission)
                .on(QStudentInfo.studentInfo.studentId.eq(QSubmission.submission.studentInfo.studentId)
                        .and(QAssignment.assignment.id.eq(QSubmission.submission.assignment.id)))
                .where(QStudentInfo.studentInfo.classroom.id.eq(classId))
                .select(QAssignment.assignment.id,QAssignment.assignment.name,QStudentInfo.studentInfo.studentId,
                        QSubmission.submission.grade)
                .fetch();
    }
}

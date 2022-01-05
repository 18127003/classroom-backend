package com.example.demo.repository.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.custom.StudentInfoClassroomCustomRepository;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class StudentInfoClassroomRepositoryImpl extends AbstractRepositoryImpl<StudentInfoClassroom> implements StudentInfoClassroomCustomRepository {
    // TODO: fix
    @Override
    public StudentInfoClassroom findByStudentId(String studentId, Long classroomId) {
        return selectFrom(QStudentInfoClassroom.studentInfoClassroom)
                .where(QStudentInfoClassroom.studentInfoClassroom.studentInfo.studentId.eq(studentId)
                        .and(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(classroomId)))
                .fetchOne();
    }

    @Override
    public List<StudentInfoClassroom> findByListStudentId(Collection<String> studentIds, Long classroomId) {
        return selectFrom(QStudentInfoClassroom.studentInfoClassroom)
                .where(QStudentInfoClassroom.studentInfoClassroom.studentInfo.studentId.in(studentIds)
                        .and(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(classroomId)))
                .fetch();
    }

    @Override
    public List<StudentInfoClassroom> findAllStudentInfo(Long classroomId) {
        return selectFrom(QStudentInfoClassroom.studentInfoClassroom)
                .where(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(classroomId)).fetch();
    }

    @Override
    public List<Tuple> getExcelData(Long classId) {
        return selectFrom(QStudentInfoClassroom.studentInfoClassroom)
                .join(QStudentInfo.studentInfo)
                .on(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id.eq(QStudentInfo.studentInfo.id))
                .where(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(classId))
                .join(QAssignment.assignment)
                .on(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(QAssignment.assignment.classroom.id))
                .leftJoin(QSubmission.submission)
                .on(QAssignment.assignment.id.eq(QSubmission.submission.assignment.id))
                .select(QAssignment.assignment.id,QAssignment.assignment.name,
                        QStudentInfo.studentInfo.studentId,
                        QSubmission.submission.grade)
                .fetch();
    }
}

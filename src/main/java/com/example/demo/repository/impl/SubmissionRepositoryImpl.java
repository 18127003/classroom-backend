package com.example.demo.repository.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.SubmissionCustomRepository;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class SubmissionRepositoryImpl extends AbstractRepositoryImpl<Submission> implements SubmissionCustomRepository {
    //TODO:fix
    @Override
    public List<Submission> getAllSubmission(Long assignmentId) {
        return selectFrom(QSubmission.submission)
                .where(QSubmission.submission.gradeComposition.assignment.id.eq(assignmentId))
                .fetch();
    }

    @Override
    public List<Submission> getSubmissionByInfoList(Collection<StudentInfoClassroom> studentInfoClassrooms, Long assignmentId) {
        return selectFrom(QSubmission.submission)
                .where(QSubmission.submission.gradeComposition.assignment.id.eq(assignmentId)
                    .and(QSubmission.submission.studentInfoClassroom.in(studentInfoClassrooms)))
                .fetch();
    }

    @Override
    public Tuple getStudentOverallGrade(Long accountId, Long classroomId) {
        return selectFrom(QSubmission.submission)
                .join(QGradeComposition.gradeComposition)
                .on(QSubmission.submission.gradeComposition.id.eq(QGradeComposition.gradeComposition.id))
                .join(QAssignment.assignment)
                .on(QGradeComposition.gradeComposition.assignment.id.eq(QAssignment.assignment.id))
                .join(QStudentInfoClassroom.studentInfoClassroom)
                .on(QSubmission.submission.studentInfoClassroom.studentInfo.id
                        .eq(QStudentInfoClassroom.studentInfoClassroom.id)
                        .and(QSubmission.submission.studentInfoClassroom.classroom.id
                                .eq(QStudentInfoClassroom.studentInfoClassroom.classroom.id)))
                .join(QStudentInfo.studentInfo)
                .on(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id.eq(QStudentInfo.studentInfo.id))
                .where(QSubmission.submission.studentInfoClassroom.classroom.id.eq(classroomId)
                        .and(QStudentInfo.studentInfo.classroomAccount.id.eq(accountId)))
                .select(QSubmission.submission.grade.sum(), QAssignment.assignment.points.sum())
                .fetchOne();
    }
}

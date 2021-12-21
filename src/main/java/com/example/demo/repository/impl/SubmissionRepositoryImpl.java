package com.example.demo.repository.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.SubmissionCustomRepository;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubmissionRepositoryImpl extends AbstractRepositoryImpl<Submission> implements SubmissionCustomRepository {
    @Override
    public List<Submission> getAllSubmission(Long assignmentId) {
        return selectFrom(QSubmission.submission).where(QSubmission.submission.assignment.id.eq(assignmentId)).fetch();
    }

    @Override
    public Tuple getStudentOverallGrade(Long accountId, Long classroomId) {
        return selectFrom(QSubmission.submission)
                .where(QSubmission.submission.assignment.classroom.id.eq(classroomId))
                .join(QStudentInfo.studentInfo)
                .on(QSubmission.submission.studentInfo.studentId.eq(QStudentInfo.studentInfo.studentId))
                .join(QAccount.account)
                .on(QStudentInfo.studentInfo.classroomAccount.id.eq(QAccount.account.id))
                .where(QAccount.account.id.eq(accountId))
                .join(QAssignment.assignment)
                .on(QSubmission.submission.assignment.id.eq(QAssignment.assignment.id))
//                .groupBy(QSubmission.submission.grade, QAssignment.assignment.points)
                .select(QSubmission.submission.grade.sum(), QAssignment.assignment.points.sum())
                .fetchOne();
    }
}

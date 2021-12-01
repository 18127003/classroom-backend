package com.example.demo.repository.impl;

import com.example.demo.entity.QAssignment;
import com.example.demo.entity.QStudentInfo;
import com.example.demo.entity.QSubmission;
import com.example.demo.entity.Submission;
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
    public List<Tuple> getSubmissionExcel(Long classId) {
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

package com.example.demo.repository.impl;

import com.example.demo.common.enums.AssignmentStatus;
import com.example.demo.entity.*;
import com.example.demo.repository.custom.SubmissionCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public class SubmissionRepositoryImpl extends AbstractRepositoryImpl<Submission> implements SubmissionCustomRepository {
    @Override
    public Submission getSubmissionByStudentId(Long assignmentId, String studentId) {
        return selectFrom(QSubmission.submission)
                .where(QSubmission.submission.assignment.id.eq(assignmentId)
                        .and(QSubmission.submission.studentInfoClassroom.studentInfo.studentId.eq(studentId)))
                .select(QSubmission.submission)
                .fetchOne();
    }

    @Override
    public List<Submission> getAllSubmission(Long assignmentId) {
        return selectFrom(QSubmission.submission)
                .where(QSubmission.submission.assignment.id.eq(assignmentId))
                .select(QSubmission.submission)
                .fetch();
    }

    @Override
    public List<Submission> getSubmissionByInfoList(Collection<StudentInfoClassroom> studentInfoClassrooms, Long assignmentId) {
        return selectFrom(QSubmission.submission)
                .where(QSubmission.submission.assignment.id.eq(assignmentId)
                    .and(QSubmission.submission.studentInfoClassroom.in(studentInfoClassrooms)))
                .select(QSubmission.submission)
                .fetch();
    }

    @Override
    public Tuple getStudentOverallGrade(UUID accountId, Long classroomId) {
        return getSubmissionJoined()
                .where(QSubmission.submission.studentInfoClassroom.classroom.id.eq(classroomId)
                        .and(QStudentInfo.studentInfo.classroomAccount.id.eq(accountId)))
                .select(QSubmission.submission.grade.sum(), QAssignment.assignment.points.sum())
                .fetchOne();
    }

    private JPAQuery<Submission> getSubmissionJoined() {
        return selectFrom(QSubmission.submission)
                .join(QAssignment.assignment)
                .on(QSubmission.submission.assignment.id.eq(QAssignment.assignment.id))
                .join(QStudentInfoClassroom.studentInfoClassroom)
                .on(QSubmission.submission.studentInfoClassroom.studentInfo.id
                        .eq(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id)
                        .and(QSubmission.submission.studentInfoClassroom.classroom.id
                                .eq(QStudentInfoClassroom.studentInfoClassroom.classroom.id)))
                .join(QStudentInfo.studentInfo)
                .on(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id.eq(QStudentInfo.studentInfo.id));
    }

    @Override
    public List<Submission> getSubmissionOfStudentByStatus(Long classroomId, String studentId, AssignmentStatus status) {
        return getSubmissionJoined()
                .where(QSubmission.submission.studentInfoClassroom.classroom.id.eq(classroomId)
                        .and(QStudentInfo.studentInfo.studentId.eq(studentId))
                        .and(QAssignment.assignment.status.eq(status)))
                .fetch();
    }

    @Override
    public List<String> checkNotSubmitStudents(Long assignmentId) {
        return selectFrom(QSubmission.submission)
                .rightJoin(QStudentInfoClassroom.studentInfoClassroom)
                .on(QSubmission.submission.studentInfoClassroom.studentInfo.id
                        .eq(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id)
                        .and(QSubmission.submission.studentInfoClassroom.classroom.id
                                .eq(QStudentInfoClassroom.studentInfoClassroom.classroom.id)))
                .join(QAssignment.assignment)
                .on(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(QAssignment.assignment.classroom.id))
                .join(QStudentInfo.studentInfo)
                .on(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id.eq(QStudentInfo.studentInfo.id))
                .where(QAssignment.assignment.id.eq(assignmentId)
                        .and(QSubmission.submission.id.isNull()))
                .select(QStudentInfo.studentInfo.studentId)
                .fetch();
    }
}

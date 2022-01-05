package com.example.demo.repository.impl;

import com.example.demo.common.enums.GradeReviewStatus;
import com.example.demo.entity.*;
import com.example.demo.repository.custom.GradeReviewCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GradeReviewRepositoryImpl extends AbstractRepositoryImpl<GradeReview> implements GradeReviewCustomRepository {
    @Override
    public GradeReview getByStatus(Long assignmentId, String studentId, GradeReviewStatus status) {
        return selectFrom(QGradeReview.gradeReview)
                .join(QSubmission.submission)
                .on(QGradeReview.gradeReview.submission.id.eq(QSubmission.submission.id))
                .join(QAssignment.assignment)
                .on(QSubmission.submission.assignment.id.eq(QAssignment.assignment.id))
                .join(QStudentInfoClassroom.studentInfoClassroom)
                .on(QSubmission.submission.studentInfoClassroom.studentInfo.id
                        .eq(QStudentInfoClassroom.studentInfoClassroom.id)
                        .and(QSubmission.submission.studentInfoClassroom.classroom.id
                                .eq(QStudentInfoClassroom.studentInfoClassroom.classroom.id)))
                .join(QStudentInfo.studentInfo)
                .on(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id.eq(QStudentInfo.studentInfo.id))
                .where(QAssignment.assignment.id.eq(assignmentId)
                        .and(QStudentInfo.studentInfo.studentId.eq(studentId))
                        .and(QGradeReview.gradeReview.status.eq(status)))
                .fetchOne();
    }

    @Override
    public List<GradeReview> getAllOfStudent(String studentId, Long classroomId) {
        return selectFrom(QGradeReview.gradeReview)
                .join(QSubmission.submission)
                .on(QGradeReview.gradeReview.submission.id.eq(QSubmission.submission.id))
                .join(QStudentInfoClassroom.studentInfoClassroom)
                .on(QSubmission.submission.studentInfoClassroom.studentInfo.id
                        .eq(QStudentInfoClassroom.studentInfoClassroom.id)
                        .and(QSubmission.submission.studentInfoClassroom.classroom.id
                                .eq(QStudentInfoClassroom.studentInfoClassroom.classroom.id)))
                .join(QStudentInfo.studentInfo)
                .on(QStudentInfoClassroom.studentInfoClassroom.studentInfo.id.eq(QStudentInfo.studentInfo.id))
                .where(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(classroomId)
                        .and(QStudentInfo.studentInfo.studentId.eq(studentId)))
                .fetch();
    }

    @Override
    public List<GradeReview> getAllOfClassroom(Long classroomId) {
        return selectFrom(QGradeReview.gradeReview)
                .join(QSubmission.submission)
                .on(QGradeReview.gradeReview.submission.id.eq(QSubmission.submission.id))
                .join(QStudentInfoClassroom.studentInfoClassroom)
                .on(QSubmission.submission.studentInfoClassroom.studentInfo.id
                        .eq(QStudentInfoClassroom.studentInfoClassroom.id)
                        .and(QSubmission.submission.studentInfoClassroom.classroom.id
                                .eq(QStudentInfoClassroom.studentInfoClassroom.classroom.id)))
                .where(QStudentInfoClassroom.studentInfoClassroom.classroom.id.eq(classroomId))
                .fetch();
    }
}

package com.example.demo.repository;

import com.example.demo.common.enums.GradeReviewStatus;
import com.example.demo.entity.GradeReview;

import java.util.List;

public interface GradeReviewCustomRepository {
    GradeReview getByStatus(Long assignmentId, String studentId, GradeReviewStatus status);

    List<GradeReview> getAllOfStudent(String studentId, Long classroomId);

    List<GradeReview> getAllOfClassroom(Long classroomId);
}

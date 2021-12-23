package com.example.demo.repository;

import com.example.demo.entity.StudentInfoClassroom;
import com.example.demo.entity.Submission;
import com.querydsl.core.Tuple;

import java.util.Collection;
import java.util.List;

public interface SubmissionCustomRepository {
    List<Submission> getAllSubmission(Long assignmentId);

    List<Submission> getSubmissionByInfoList(Collection<StudentInfoClassroom> studentInfoClassrooms, Long assignmentId);

    Tuple getStudentOverallGrade(Long accountId, Long classroomId);
}

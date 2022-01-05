package com.example.demo.repository.custom;

import com.example.demo.common.enums.AssignmentStatus;
import com.example.demo.entity.StudentInfoClassroom;
import com.example.demo.entity.Submission;
import com.querydsl.core.Tuple;

import java.util.Collection;
import java.util.List;

public interface SubmissionCustomRepository {
    Submission getSubmissionByStudentId(Long assignmentId, String studentId);

    List<Submission> getAllSubmission(Long assignmentId);

    List<Submission> getSubmissionByInfoList(Collection<StudentInfoClassroom> studentInfoClassrooms, Long assignmentId);

    Tuple getStudentOverallGrade(Long accountId, Long classroomId);

    List<Submission> getSubmissionOfStudentByStatus(Long classroomId, String studentId, AssignmentStatus status);

    List<String> checkNotSubmitStudents(Long assignmentId);
}

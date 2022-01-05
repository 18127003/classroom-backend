package com.example.demo.service;

import com.example.demo.dto.OverallGradeDto;
import com.example.demo.dto.SubmissionDto;
import com.example.demo.entity.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AssignmentService {
    Assignment getAssignment(Long id);

    List<Assignment> getAllAssignments(Long classroomId);

    Assignment addAssignment(Assignment assignment, Account creator, Classroom classroom);

    void removeAssignment(Long id);

    Assignment updateAssignment(Long id, Assignment update);

    void updatePosition(Long id, List<Long> update);

    StudentInfoClassroom addStudentInfo(StudentInfoClassroom studentInfo);

    Submission addSubmission(SubmissionDto submissionDto, Long classroomId);

    void exportTemplateFile(HttpServletResponse response, Long classroomId) throws IOException;

    void importSubmission(MultipartFile file, Classroom classroom, Long assignmentId) throws IOException;

    Submission updateSubmissionGrade(Long submissionId, Integer grade);

    OverallGradeDto getOverallGrade(Long accountId, Long classroomId);

    List<Submission> getGradeOfClassByStudent(Account account, Long classroomId);

    void finalizeGrade(Long assignmentId);

    Submission getSubmission(Long assignmentId, String studentId);

    GradeReview addGradeReview(GradeReview gradeReview, Submission submission, Account account);

    GradeReview getPendingGradeReview(Long assignmentId, String studentId);

    List<GradeReview> getAllGradeReview(Long assignmentId);

    List<GradeReview> getAllGradeReviewOfClass(Long classroomId);

    List<GradeReview> getAllGradeReviewOfAccount(String studentId, Long classroomId);

    List<String> checkFillSubmission(Long assignmentId);

    Comment createReviewComment(Comment comment, Long reviewId, Account account);

    void finalizeGradeReview(Long gradeReviewId, Integer grade);

    GradeReview getGradeReview(Long gradeReviewId);
}

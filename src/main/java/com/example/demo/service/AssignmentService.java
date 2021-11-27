package com.example.demo.service;

import com.example.demo.dto.SubmissionDto;
import com.example.demo.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AssignmentService {
    Assignment getAssignment(Long id);

    List<Assignment> getAllAssignments(Long classroomId);

    Assignment addAssignment(Assignment assignment, Account creator, Classroom classroom);

    void removeAssignment(Long id);

    Assignment updateAssignment(Long id, Assignment update);

    void updatePosition(Long id, List<Long> update);

    StudentInfo addStudentInfo(StudentInfo studentInfo);

    Submission addSubmission(SubmissionDto submissionDto);

    StudentInfo getStudentInfo(String studentId);

    List<StudentInfo> getAllStudentInfo(Long classroomId);

    void importStudentInfo(MultipartFile file, Classroom classroom) throws IOException;
}

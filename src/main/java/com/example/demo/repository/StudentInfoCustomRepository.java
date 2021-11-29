package com.example.demo.repository;

import com.example.demo.entity.StudentInfo;

import java.util.List;

public interface StudentInfoCustomRepository {
    StudentInfo findByStudentId(String studentId, Long classroomId);

    List<StudentInfo> findAllStudentInfo(Long classroomId);
}

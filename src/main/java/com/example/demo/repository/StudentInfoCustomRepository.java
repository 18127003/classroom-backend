package com.example.demo.repository;

import com.example.demo.entity.StudentInfo;
import com.querydsl.core.Tuple;

import java.util.List;

public interface StudentInfoCustomRepository {
    StudentInfo findByStudentId(String studentId, Long classroomId);

    List<StudentInfo> findAllStudentInfo(Long classroomId);

    List<Tuple> getExcelData(Long classId);
}

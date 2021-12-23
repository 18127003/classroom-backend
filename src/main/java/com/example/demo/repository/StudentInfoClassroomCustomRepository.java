package com.example.demo.repository;

import com.example.demo.entity.StudentInfo;
import com.example.demo.entity.StudentInfoClassroom;
import com.querydsl.core.Tuple;

import java.util.Collection;
import java.util.List;

public interface StudentInfoClassroomCustomRepository {
    StudentInfoClassroom findByStudentId(String studentId, Long classroomId);

    List<StudentInfoClassroom> findByListStudentId(Collection<String> studentIds, Long classroomId);

    List<StudentInfoClassroom> findAllStudentInfo(Long classroomId);

    List<Tuple> getExcelData(Long classId);
}

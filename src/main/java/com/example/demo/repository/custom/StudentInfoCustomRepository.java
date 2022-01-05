package com.example.demo.repository.custom;

import com.example.demo.entity.StudentInfo;

import java.util.Collection;
import java.util.List;

public interface StudentInfoCustomRepository {
    List<StudentInfo> findByListStudentId(Collection<String> studentIds);

    StudentInfo findByStudentId(String studentId);
}

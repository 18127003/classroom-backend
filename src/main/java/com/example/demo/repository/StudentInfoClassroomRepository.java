package com.example.demo.repository;

import com.example.demo.entity.StudentInfo;
import com.example.demo.entity.StudentInfoClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StudentInfoClassroomRepository extends JpaRepository<StudentInfoClassroom, Long>,
        QuerydslPredicateExecutor<StudentInfoClassroom>, StudentInfoClassroomCustomRepository {
}

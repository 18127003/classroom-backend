package com.example.demo.repository;

import com.example.demo.entity.StudentInfo;
import com.example.demo.repository.custom.StudentInfoCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long>,
        QuerydslPredicateExecutor<StudentInfo>, StudentInfoCustomRepository {
}

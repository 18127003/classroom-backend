package com.example.demo.repository;

import com.example.demo.entity.Assignment;
import com.example.demo.repository.custom.AssignmentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AssignmentRepository extends JpaRepository<Assignment, Long>, QuerydslPredicateExecutor<Assignment>, AssignmentCustomRepository {
}

package com.example.demo.repository;

import com.example.demo.entity.Submission;
import com.example.demo.repository.custom.SubmissionCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubmissionRepository extends JpaRepository<Submission, Long>, QuerydslPredicateExecutor<Submission>, SubmissionCustomRepository {
}

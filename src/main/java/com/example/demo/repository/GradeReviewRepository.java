package com.example.demo.repository;

import com.example.demo.entity.GradeReview;
import com.example.demo.repository.custom.GradeReviewCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GradeReviewRepository extends JpaRepository<GradeReview, Long>,
        QuerydslPredicateExecutor<GradeReview>, GradeReviewCustomRepository {
}

package com.example.demo.repository;

import com.example.demo.entity.GradeComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GradeCompositionRepository extends JpaRepository<GradeComposition, Long>,
        QuerydslPredicateExecutor<GradeComposition>{
}

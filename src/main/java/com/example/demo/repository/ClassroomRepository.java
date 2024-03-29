package com.example.demo.repository;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.custom.ClassroomCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ClassroomRepository extends JpaRepository<Classroom, Long>, QuerydslPredicateExecutor<Classroom>, ClassroomCustomRepository {
}

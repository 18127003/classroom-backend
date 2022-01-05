package com.example.demo.repository;

import com.example.demo.entity.VerifyToken;
import com.example.demo.repository.custom.VerifyTokenCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long>, QuerydslPredicateExecutor<VerifyToken>, VerifyTokenCustomRepository {
}

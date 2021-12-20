package com.example.demo.repository;

import com.example.demo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AdminRepository extends JpaRepository<Admin, Long>, QuerydslPredicateExecutor<Admin>, AdminCustomRepository{
}

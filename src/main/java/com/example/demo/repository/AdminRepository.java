package com.example.demo.repository;

import com.example.demo.entity.Admin;
import com.example.demo.repository.custom.AdminCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID>, QuerydslPredicateExecutor<Admin>, AdminCustomRepository {
}

package com.example.demo.repository;

import com.example.demo.entity.LockedAccount;
import com.example.demo.repository.custom.LockedAccountCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface LockedAccountRepository extends JpaRepository<LockedAccount, Long>, QuerydslPredicateExecutor<LockedAccount>, LockedAccountCustomRepository {
}

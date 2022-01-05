package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.repository.custom.AccountCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account>, AccountCustomRepository {
}

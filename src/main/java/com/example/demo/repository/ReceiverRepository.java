package com.example.demo.repository;

import com.example.demo.entity.Receiver;
import com.example.demo.repository.custom.ReceiverCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReceiverRepository extends JpaRepository<Receiver, Long>, QuerydslPredicateExecutor<Receiver>, ReceiverCustomRepository {
}

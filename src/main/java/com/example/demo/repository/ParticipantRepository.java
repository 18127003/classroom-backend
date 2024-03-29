package com.example.demo.repository;

import com.example.demo.entity.Participant;
import com.example.demo.repository.custom.ParticipantCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, QuerydslPredicateExecutor<Participant>, ParticipantCustomRepository {
}

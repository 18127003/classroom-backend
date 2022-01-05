package com.example.demo.repository.custom;

import com.example.demo.entity.Participant;

import java.util.List;

public interface ParticipantCustomRepository {
    List<Participant> getParticipants(Long classId);

    List<Participant> getAssignedClassrooms(Long accountId);

    Participant findParticipant(Long classId, Long accountId);
}

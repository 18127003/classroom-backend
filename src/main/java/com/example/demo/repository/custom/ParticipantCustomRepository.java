package com.example.demo.repository.custom;

import com.example.demo.entity.Participant;

import java.util.List;
import java.util.UUID;

public interface ParticipantCustomRepository {
    List<Participant> getParticipants(Long classId);

    List<Participant> getAssignedClassrooms(UUID accountId);

    Participant findParticipant(Long classId, UUID accountId);
}

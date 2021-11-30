package com.example.demo.repository;

import com.example.demo.entity.Participant;

import java.util.List;

public interface ParticipantCustomRepository {
    List<Participant> getParticipants(Long classId);

    List<Participant> getAssignedClassroom(Long accountId);

    Participant findParticipant(Long classId, Long accountId);

    Participant findByStudentId(Long classId, String studentId);
}

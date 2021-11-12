package com.example.demo.repository.impl;

import com.example.demo.entity.Participant;
import com.example.demo.entity.QParticipant;
import com.example.demo.repository.ParticipantCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParticipantRepositoryImpl extends AbstractRepositoryImpl<Participant> implements ParticipantCustomRepository {
    @Override
    public List<Participant> getParticipants(Long classId) {
        return selectFrom(QParticipant.participant)
                .where(QParticipant.participant.classroom.id.eq(classId))
                .orderBy(QParticipant.participant.account.lastName.asc())
                .fetch();
    }

    @Override
    public List<Participant> getAssignedClassroom(Long accountId) {
        return selectFrom(QParticipant.participant)
                .where(QParticipant.participant.account.id.eq(accountId))
                .fetch();
    }
}

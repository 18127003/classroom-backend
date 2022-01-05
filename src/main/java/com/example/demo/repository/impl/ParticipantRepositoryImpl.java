package com.example.demo.repository.impl;

import com.example.demo.entity.Participant;
import com.example.demo.entity.QParticipant;
import com.example.demo.repository.custom.ParticipantCustomRepository;
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
    public List<Participant> getAssignedClassrooms(Long accountId) {
        return selectFrom(QParticipant.participant)
                .where(QParticipant.participant.account.id.eq(accountId))
                .fetch();
    }

    @Override
    public Participant findParticipant(Long classId, Long accountId) {
        return selectFrom(QParticipant.participant)
                .where(QParticipant.participant.classroom.id.eq(classId)
                .and(QParticipant.participant.account.id.eq(accountId)))
                .fetchOne();
    }
}

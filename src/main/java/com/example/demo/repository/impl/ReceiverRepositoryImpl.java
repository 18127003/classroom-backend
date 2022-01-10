package com.example.demo.repository.impl;

import com.example.demo.entity.Notification;
import com.example.demo.entity.QNotification;
import com.example.demo.entity.QReceiver;
import com.example.demo.entity.Receiver;
import com.example.demo.repository.custom.ReceiverCustomRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ReceiverRepositoryImpl extends AbstractRepositoryImpl<Receiver> implements ReceiverCustomRepository {
    @Override
    public List<Notification> getAllOfAccount(UUID accountId) {
        return selectFrom(QReceiver.receiver)
                .where(QReceiver.receiver.account.id.eq(accountId))
                .select(QReceiver.receiver.notification)
                .fetch();
    }
}

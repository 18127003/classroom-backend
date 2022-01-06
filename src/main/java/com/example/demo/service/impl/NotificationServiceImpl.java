package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Receiver;
import com.example.demo.repository.ReceiverRepository;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ReceiverRepository receiverRepository;

    @Override
    public void sendNotification(List<Account> accounts, String content) {
        var notification = new Notification(content);
        var receiver = accounts.stream().map(account->new Receiver(notification, account))
                .collect(Collectors.toList());
        receiverRepository.saveAll(receiver);
        //TODO: socket send
    }
}

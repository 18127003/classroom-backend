package com.example.demo.service.impl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Receiver;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.repository.ReceiverRepository;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {Throwable.class})
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final ReceiverRepository receiverRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationMapper notificationMapper;

    @Override
    public void sendNotification(List<Account> accounts, String content) {
        var notification = new Notification(content);
        var receivers = accounts.stream().map(account->new Receiver(notification, account))
                .collect(Collectors.toList());
        receiverRepository.saveAll(receivers);
        //TODO: socket send
        receivers.forEach(receiver ->
                simpMessagingTemplate.convertAndSendToUser(receiver.getAccount().getId().toString(),
                        "/topic/noti",
                        notificationMapper.toNotificationDto(receiver.getNotification())));
    }


}

package com.example.demo.repository.custom;

import com.example.demo.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface ReceiverCustomRepository {
    List<Notification> getAllOfAccount(UUID accountId);
}

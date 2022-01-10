package com.example.demo.service;


import com.example.demo.entity.Account;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    void sendNotification(List<Account> accounts, String content);
}

package com.example.demo.service;


import com.example.demo.entity.Account;

import java.util.List;

public interface NotificationService {
    void sendNotification(List<Account> accounts, String content);
}

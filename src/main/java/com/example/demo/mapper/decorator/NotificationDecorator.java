package com.example.demo.mapper.decorator;

import com.example.demo.dto.NotificationDto;
import com.example.demo.entity.Notification;
import com.example.demo.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class NotificationDecorator implements NotificationMapper {
    @Autowired
    @Qualifier("delegate")
    private NotificationMapper delegate;

    @Override
    public NotificationDto toNotificationDto(Notification notification) {
        return delegate.toNotificationDto(notification);
    }
}

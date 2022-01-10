package com.example.demo.mapper;

import com.example.demo.dto.NotificationDto;
import com.example.demo.entity.Notification;
import com.example.demo.mapper.decorator.NotificationDecorator;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(NotificationDecorator.class)
public interface NotificationMapper {
    NotificationDto toNotificationDto(Notification notification);
}

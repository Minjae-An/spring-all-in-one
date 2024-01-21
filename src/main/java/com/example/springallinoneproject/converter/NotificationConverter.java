package com.example.springallinoneproject.converter;

import com.example.springallinoneproject.notification.domain.Notification;
import com.example.springallinoneproject.notification.NotificationResponse.CreateNotificationDTO;

public class NotificationConverter {
    public static CreateNotificationDTO toCreateNotificationDTO(Notification notification){
        return CreateNotificationDTO.builder()
                .notificationId(notification.getId())
                .content(notification.getContent())
                .relatedUrl(notification.getRelatedUrl())
                .notificationType(notification.getNotificationType())
                .build();
    }
}

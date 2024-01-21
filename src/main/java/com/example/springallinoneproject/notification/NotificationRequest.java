package com.example.springallinoneproject.notification;

import com.example.springallinoneproject.notification.domain.NotificationType;
import lombok.Getter;

public class NotificationRequest {
    @Getter
    public static class CreateNotificationRequest {
        private String content;
        private String relatedUrl;
        private NotificationType notificationType;
    }

}

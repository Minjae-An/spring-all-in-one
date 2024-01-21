package com.example.springallinoneproject.notification.dto;

import com.example.springallinoneproject.notification.domain.NotificationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateNotificationDTO{
        private Long notificationId;
        private String content;
        private String relatedUrl;
        private NotificationType notificationType;
    }
}

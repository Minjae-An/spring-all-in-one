package com.example.springallinoneproject.notification;

import com.example.springallinoneproject.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

package com.example.springallinoneproject.notification;

import com.example.springallinoneproject.notification.NotificationRequest.CreateNotificationRequest;
import com.example.springallinoneproject.user.entity.User;
import com.example.springallinoneproject.user.service.UserQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;
    private final UserQueryService userQueryService;

    @EventListener
    public void handleNotificationForAll(CreateNotificationRequest event){
        List<User> users = userQueryService.findAll();
        users.forEach(user -> notificationService.send(
                user,
                event.getNotificationType(),
                event.getContent(),
                event.getRelatedUrl()
        ));
    }
}

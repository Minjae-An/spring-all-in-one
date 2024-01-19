package com.example.springallinoneproject.notification;

import com.example.springallinoneproject.notification.NotificationRequest.CreateNotificationRequest;
import com.example.springallinoneproject.user.entity.User;
import com.example.springallinoneproject.user.service.UserQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final UserQueryService userQueryService;
    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
            @RequestParam("userId") Long userId) {
        return notificationService.subscribe(userId, lastEventId);
    }

    @PostMapping("/notification/{receiverId}")
    public ResponseEntity<Void> notification(@PathVariable("receiverId") Long receiverId
            , @RequestBody CreateNotificationRequest request) {
        User receiver = userQueryService.findById(receiverId);
        notificationService.send(receiver, request.getNotificationType(),
                request.getContent(), request.getRelatedUrl());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications")
    public ResponseEntity<Void> notificationToAll(@RequestBody CreateNotificationRequest request) {
        List<User> users = userQueryService.findAll();
        users.forEach(user -> notificationService
                .send(user, request.getNotificationType(),
                        request.getContent(), request.getRelatedUrl()));

        return ResponseEntity.ok().build();
    }
}

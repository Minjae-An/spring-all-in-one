package com.example.springallinoneproject.notification;

import com.example.springallinoneproject.converter.NotificationConverter;
import com.example.springallinoneproject.notification.emitter.EmitterRepository;
import com.example.springallinoneproject.user.entity.User;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final Long timeoutMillis = 600_000L;

    public SseEmitter subscribe(Long userId, String lastEventId) {
        String emitterId = makeTimeIncludeId(userId);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeoutMillis));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId,
                emitterId, "EventStream Created. [userId=%d]".formatted(userId));

        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    public void send(User receiver, NotificationType notificationType, String content, String relatedUrl) {
        Notification notification = notificationRepository
                .save(createNotification(receiver, notificationType, content, relatedUrl));

        String receiverId = String.valueOf(receiver.getId());
        String eventId = makeTimeIncludeId(receiver.getId());
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(receiverId);
        emitters.forEach(
                (id, emitter) -> {
                    emitterRepository.saveEventCache(id, notification);
                    sendNotification(emitter, eventId, id,
                            NotificationConverter.toCreateNotificationDTO(notification));
                }
        );
    }

    private String makeTimeIncludeId(Long id) {
        return id + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository
                .findAllEventCacheByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    private Notification createNotification(User receiver, NotificationType notificationType,
                                            String content, String url) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .relatedUrl(url)
                .isRead(false)
                .build();
    }
}

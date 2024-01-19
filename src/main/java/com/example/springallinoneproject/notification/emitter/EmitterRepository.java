package com.example.springallinoneproject.notification.emitter;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void saveEventCache(String eventCacheId, Object event);
    Map<String, SseEmitter> findAllEmitterByUserId(String userId);
    Map<String, Object> findAllEventCacheByUserId(String userId);
    void deleteById(String emitterId);
    void deleteAllEmitterByUserId(String userId);
    void deleteAllEventCacheByUserId(String userId);
}

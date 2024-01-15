package com.example.springallinoneproject.util;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private RedisTemplate<String, Object> redisTemplate;

    public void putData(String key, String value, long expiredTime) {
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MICROSECONDS);
    }

    public String getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}

package com.example.redisdistributedrock.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class LockAdapter {
    private final RedisTemplate<String, Long> lockRedisTemplate;
    private final ValueOperations<String, Long> lockOperation;

    public LockAdapter(RedisTemplate<String, Long> lockRedisTemplate, ValueOperations<String, Long> lockOperation) {
        this.lockRedisTemplate = lockRedisTemplate;
        this.lockOperation = lockRedisTemplate.opsForValue();
    }

    public boolean holdLock(Long ticketId, Long userId) {
        return lockOperation.setIfAbsent("LOCK::" + ticketId, userId, Duration.ofSeconds(10));
    }

    public Long checkLock(Long ticketId) {
        return lockOperation.get("LOCK::" + ticketId);
    }

    public void unlock(Long ticketId) {
        lockRedisTemplate.delete("LOCK::" + ticketId);
    }
}

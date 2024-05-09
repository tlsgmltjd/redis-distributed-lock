package com.example.redisdistributedrock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {

    private final RedissonClient redissonClient;
    private final int EMPTY = 0;

    public void useTicketNoLock(final String key) {
        final int quantity = getTicketQuantity(key);

        if (quantity <= EMPTY) {
            log.info("티켓 모두 소진");
            return;
        }

        log.info("남은 티켓 : {}", quantity);

        buy(key, quantity - 1);
    }

    public void buy(String key, int quantity) {
        redissonClient.getBucket(key).set(quantity);
    }

    public int getTicketQuantity(String key) {
        return (int) redissonClient.getBucket(key).get();
    }
}

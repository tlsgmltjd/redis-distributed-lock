package com.example.redisdistributedrock.service;

import com.example.redisdistributedrock.domain.Ticket;
import com.example.redisdistributedrock.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final RedissonClient redissonClient;

    @Transactional
    public void buy(Long id) {
        log.info("====================== START: {} ======================", Thread.currentThread().getName());

        if (ticketRepository.existsByUserId(id)) {
            log.error("이미 티켓을 구매하였습니다.");
            return;
        }

        Ticket ticket = new Ticket(null, id);
        ticketRepository.save(ticket);

        log.info("====================== END: {} ======================", Thread.currentThread().getName());
    }

    @Transactional
    public void buy_redisson_lock(Long id) {

        RLock rLock = redissonClient.getLock("test");

        try {
            boolean available = rLock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                log.error("===== redisson lock timeout =====");
            }

            log.info("====================== START: {} ======================", Thread.currentThread().getName());

            if (ticketRepository.existsByUserId(id)) {
                log.error("이미 티켓을 구매하였습니다.");
                return;
            }

            Ticket ticket = new Ticket(null, id);
            ticketRepository.save(ticket);

            log.info("====================== END: {} ======================", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
        }
    }
}

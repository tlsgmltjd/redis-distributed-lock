package com.example.redisdistributedrock.service;

import com.example.redisdistributedrock.domain.Ticket;
import com.example.redisdistributedrock.repository.TicketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceTest {

    @Autowired
    TicketService ticketService;
    @Autowired
    TicketRepository ticketRepository;


    int threadCount = 10;

    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(threadCount);

    @BeforeEach
    void init() {
        ticketRepository.deleteAll();
        ticketRepository.save(new Ticket(1L, "hi", 30));
    }

    @Test
    void 동시성_문제_발생_CASE() {

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {

                try {
                    ticketService.buy(1L);
                } finally {
                    latch.countDown();
                }

            });
        }

        Assertions.assertThat(ticketRepository.findById(1L).get().getQuantity()).isEqualTo(20);

    }
    @Test
    void 동시성_문제_안발생_CASE() {

        for (int i = 0; i < threadCount; i++) {
           ticketService.buy(1L);
        }

        Assertions.assertThat(ticketRepository.findById(1L).get().getQuantity()).isEqualTo(20);

    }

}
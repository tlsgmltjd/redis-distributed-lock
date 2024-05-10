package com.example.redisdistributedrock.service;

import com.example.redisdistributedrock.domain.Ticket;
import com.example.redisdistributedrock.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;

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
}

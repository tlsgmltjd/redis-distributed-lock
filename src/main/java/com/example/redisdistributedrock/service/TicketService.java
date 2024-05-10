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

        Ticket ticket = ticketRepository.findById(id).orElse(null);

        if (ticket != null && ticket.getQuantity() <= 0) {
            log.warn("매진");
        }

        ticket.setQuantity(ticket.getQuantity() - 1);
        log.info("구매 : " + ticket.getQuantity());
        ticketRepository.save(ticket);

    }
}

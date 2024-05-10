package com.example.redisdistributedrock.repository;

import com.example.redisdistributedrock.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByUserId(Long userId);
}

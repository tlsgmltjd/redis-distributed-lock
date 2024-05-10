package com.example.redisdistributedrock.repository;

import com.example.redisdistributedrock.domain.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByUserId(Long userId);
}

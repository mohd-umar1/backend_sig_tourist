package com.tourist.safety.repository;

import com.tourist.safety.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketCode(String ticketCode);
    List<Ticket> findByTouristId(String touristId);
    List<Ticket> findByStatus(Ticket.TicketStatus status);
    boolean existsByTicketCode(String ticketCode);
}

package com.tourist.safety.service;

import com.tourist.safety.entity.Ticket;
import com.tourist.safety.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Transactional
    public Ticket createTicket(String touristId, String destination, 
                              LocalDateTime validFrom, LocalDateTime validUntil,
                              Double centerLat, Double centerLng, Integer radiusMeters) {
        String ticketCode = generateTicketCode();
        
        Ticket ticket = new Ticket(ticketCode, touristId, destination, validFrom, validUntil, 
                                  centerLat, centerLng, radiusMeters);
        ticket = ticketRepository.save(ticket);
        
        // Create audit block
        String eventData = String.format("{\"ticketCode\":\"%s\",\"touristId\":\"%s\",\"destination\":\"%s\"}", 
            ticketCode, touristId, destination);
        auditService.createAuditBlock("TICKET_CREATED", eventData);
        
        return ticket;
    }
    
    public Optional<Ticket> getTicketByCode(String ticketCode) {
        return ticketRepository.findByTicketCode(ticketCode);
    }
    
    public List<Ticket> getTicketsByTouristId(String touristId) {
        return ticketRepository.findByTouristId(touristId);
    }
    
    public List<Ticket> getActiveTickets() {
        return ticketRepository.findByStatus(Ticket.TicketStatus.ACTIVE);
    }
    
    @Transactional
    public Ticket verifyTicket(String ticketCode) {
        Optional<Ticket> ticketOpt = ticketRepository.findByTicketCode(ticketCode);
        if (ticketOpt.isEmpty()) {
            return null;
        }
        
        Ticket ticket = ticketOpt.get();
        
        // Check if ticket is active
        if (ticket.getStatus() != Ticket.TicketStatus.ACTIVE) {
            return ticket;
        }
        
        // Check validity period
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(ticket.getValidFrom()) || now.isAfter(ticket.getValidUntil())) {
            ticket.setStatus(Ticket.TicketStatus.EXPIRED);
            ticketRepository.save(ticket);
        }
        
        // Create audit block
        String eventData = String.format("{\"ticketCode\":\"%s\",\"status\":\"%s\"}", 
            ticketCode, ticket.getStatus());
        auditService.createAuditBlock("TICKET_VERIFIED", eventData);
        
        return ticket;
    }
    
    private String generateTicketCode() {
        String code;
        do {
            code = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (ticketRepository.existsByTicketCode(code));
        return code;
    }
}

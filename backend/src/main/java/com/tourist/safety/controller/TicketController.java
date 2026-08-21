package com.tourist.safety.controller;

import com.tourist.safety.entity.Ticket;
import com.tourist.safety.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody Map<String, Object> request) {
        try {
            Ticket ticket = ticketService.createTicket(
                (String) request.get("touristId"),
                (String) request.get("type"),
                LocalDateTime.parse((String) request.get("validFrom")),
                LocalDateTime.parse((String) request.get("validUntil")),
                request.get("centerLat") != null ? ((Number) request.get("centerLat")).doubleValue() : null,
                request.get("centerLng") != null ? ((Number) request.get("centerLng")).doubleValue() : null,
                request.get("radiusMeters") != null ? ((Number) request.get("radiusMeters")).intValue() : 2000
            );
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> verifyTicket(@RequestBody Map<String, String> request) {
        Ticket ticket = ticketService.verifyTicket(request.get("ticketCode"));
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }
    
    @GetMapping("/{ticketCode}")
    public ResponseEntity<?> getTicket(@PathVariable String ticketCode) {
        return ticketService.getTicketByCode(ticketCode)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/tourist/{touristId}")
    public ResponseEntity<List<Ticket>> getTicketsByTourist(@PathVariable String touristId) {
        return ResponseEntity.ok(ticketService.getTicketsByTouristId(touristId));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Ticket>> getActiveTickets() {
        return ResponseEntity.ok(ticketService.getActiveTickets());
    }
}

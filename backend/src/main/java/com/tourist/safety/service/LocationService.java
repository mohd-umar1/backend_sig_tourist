package com.tourist.safety.service;

import com.tourist.safety.entity.Incident;
import com.tourist.safety.entity.Ticket;
import com.tourist.safety.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private IncidentService incidentService;
    
    public LocationCheckResult checkLocation(String ticketCode, Double currentLat, Double currentLng) {
        Optional<Ticket> ticketOpt = ticketRepository.findByTicketCode(ticketCode);
        if (ticketOpt.isEmpty()) {
            return new LocationCheckResult(false, "Ticket not found", null);
        }
        
        Ticket ticket = ticketOpt.get();
        
        if (ticket.getStatus() != Ticket.TicketStatus.ACTIVE) {
            return new LocationCheckResult(false, "Ticket is not active", ticket);
        }
        
        Double distance = calculateDistance(
            currentLat, currentLng,
            ticket.getCenterLat(), ticket.getCenterLng()
        );
        
        boolean isWithinRadius = distance <= ticket.getRadiusMeters();
        
        if (!isWithinRadius) {
            // Create geofence violation incident
            incidentService.createIncident(
                ticket.getTouristId(),
                com.tourist.safety.entity.Incident.IncidentType.GEOFENCE_VIOLATION,
                com.tourist.safety.entity.Incident.Severity.MEDIUM,
                currentLat,
                currentLng,
                "Tourist moved outside permitted area. Distance: " + Math.round(distance) + "m"
            );
            
            return new LocationCheckResult(false, 
                "GEO-FENCE VIOLATION: You are " + Math.round(distance) + "m from center (limit: " + 
                ticket.getRadiusMeters() + "m)", ticket);
        }
        
        return new LocationCheckResult(true, "SAFE: You are within the permitted area", ticket);
    }
    
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371000; // Earth's radius in meters
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
    
    public static class LocationCheckResult {
        private boolean safe;
        private String message;
        private Ticket ticket;
        
        public LocationCheckResult(boolean safe, String message, Ticket ticket) {
            this.safe = safe;
            this.message = message;
            this.ticket = ticket;
        }
        
        // Getters
        public boolean isSafe() { return safe; }
        public String getMessage() { return message; }
        public Ticket getTicket() { return ticket; }
    }
}

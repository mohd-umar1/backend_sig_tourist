package com.tourist.safety.config;

import com.tourist.safety.entity.Incident;
import com.tourist.safety.entity.Ticket;
import com.tourist.safety.entity.Tourist;
import com.tourist.safety.repository.IncidentRepository;
import com.tourist.safety.repository.TicketRepository;
import com.tourist.safety.repository.TouristRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private TouristRepository touristRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Only seed if database is empty
        if (touristRepository.count() == 0) {
            System.out.println("Seeding database with demo data...");
            
            // Create demo tourist
            Tourist tourist = new Tourist(
                "TID-DEMO01",
                "John Smith",
                "john.smith@example.com",
                "+1-555-0101",
                "USA",
                "+1-555-0102"
            );
            touristRepository.save(tourist);
            
            // Create demo ticket (valid for 7 days, centered on Paris)
            LocalDateTime now = LocalDateTime.now();
            Ticket ticket = new Ticket(
                "TKT-DEMO01",
                "TID-DEMO01",
                "Paris, France",
                now,
                now.plusDays(7),
                48.8566,  // Paris latitude
                2.3522,   // Paris longitude
                5000      // 5km radius
            );
            ticketRepository.save(ticket);
            
            // Create demo incident
            Incident incident = new Incident(
                "TID-DEMO01",
                com.tourist.safety.entity.Incident.IncidentType.SOS,
                com.tourist.safety.entity.Incident.Severity.HIGH,
                48.8566,
                2.3522,
                "Demo SOS incident"
            );
            incidentRepository.save(incident);
            
            System.out.println("Demo data seeded successfully!");
        }
    }
}

package com.tourist.safety.service;

import com.tourist.safety.entity.Incident;
import com.tourist.safety.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Transactional
    public Incident createIncident(String touristId, Incident.IncidentType type, 
                                   Incident.Severity severity, Double latitude, 
                                   Double longitude, String description) {
        Incident incident = new Incident(touristId, type, severity, latitude, longitude, description);
        incident = incidentRepository.save(incident);
        
        // Create audit block
        String eventData = String.format("{\"touristId\":\"%s\",\"type\":\"%s\",\"severity\":\"%s\"}", 
            touristId, type, severity);
        auditService.createAuditBlock(type == Incident.IncidentType.SOS ? "SOS_TRIGGERED" : "GEOFENCE_VIOLATION", eventData);
        
        return incident;
    }
    
    @Transactional
    public Optional<Incident> resolveIncident(Long incidentId) {
        Optional<Incident> incidentOpt = incidentRepository.findById(incidentId);
        if (incidentOpt.isPresent()) {
            Incident incident = incidentOpt.get();
            incident.setStatus(Incident.IncidentStatus.RESOLVED);
            incident.setResolvedAt(LocalDateTime.now());
            incident = incidentRepository.save(incident);
            
            // Create audit block
            String eventData = String.format("{\"incidentId\":\"%d\",\"touristId\":\"%s\"}", 
                incidentId, incident.getTouristId());
            auditService.createAuditBlock("INCIDENT_RESOLVED", eventData);
            
            return Optional.of(incident);
        }
        return Optional.empty();
    }
    
    public List<Incident> getIncidentsByTouristId(String touristId) {
        return incidentRepository.findByTouristId(touristId);
    }
    
    public List<Incident> getActiveIncidents() {
        return incidentRepository.findByStatus(Incident.IncidentStatus.OPEN);
    }
    
    public List<Incident> getAllIncidents() {
        return incidentRepository.findByOrderByCreatedAtDesc();
    }
    
    public Optional<Incident> getIncidentById(Long id) {
        return incidentRepository.findById(id);
    }
}

package com.tourist.safety.controller;

import com.tourist.safety.entity.Incident;
import com.tourist.safety.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
public class IncidentController {
    
    @Autowired
    private IncidentService incidentService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createIncident(@RequestBody Map<String, Object> request) {
        try {
            Incident incident = incidentService.createIncident(
                (String) request.get("touristId"),
                Incident.IncidentType.valueOf((String) request.get("type")),
                request.get("severity") != null ? 
                    Incident.Severity.valueOf((String) request.get("severity")) : 
                    Incident.Severity.HIGH,
                request.get("latitude") != null ? ((Number) request.get("latitude")).doubleValue() : null,
                request.get("longitude") != null ? ((Number) request.get("longitude")).doubleValue() : null,
                (String) request.get("description")
            );
            return ResponseEntity.ok(incident);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{incidentId}/resolve")
    public ResponseEntity<?> resolveIncident(@PathVariable Long incidentId) {
        return incidentService.resolveIncident(incidentId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getIncident(@PathVariable Long id) {
        return incidentService.getIncidentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/tourist/{touristId}")
    public ResponseEntity<List<Incident>> getIncidentsByTourist(@PathVariable String touristId) {
        return ResponseEntity.ok(incidentService.getIncidentsByTouristId(touristId));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Incident>> getActiveIncidents() {
        return ResponseEntity.ok(incidentService.getActiveIncidents());
    }
    
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }
}

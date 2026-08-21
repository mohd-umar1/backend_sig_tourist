package com.tourist.safety.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String touristId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncidentType type;
    
    @Enumerated(EnumType.STRING)
    private Severity severity = Severity.HIGH;
    
    private Double latitude;
    private Double longitude;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private IncidentStatus status = IncidentStatus.OPEN;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    public Incident() {}
    
    public Incident(String touristId, IncidentType type, Severity severity, 
                   Double latitude, Double longitude, String description) {
        this.touristId = touristId;
        this.type = type;
        this.severity = severity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTouristId() { return touristId; }
    public void setTouristId(String touristId) { this.touristId = touristId; }
    
    public IncidentType getType() { return type; }
    public void setType(IncidentType type) { this.type = type; }
    
    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    
    public enum IncidentType {
        SOS, GEOFENCE_VIOLATION
    }
    
    public enum Severity {
        LOW, MEDIUM, HIGH
    }
    
    public enum IncidentStatus {
        OPEN, RESOLVED
    }
}

package com.tourist.safety.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String ticketCode;
    
    @Column(nullable = false)
    private String touristId;
    
    private String type;
    
    @Column(name = "valid_from")
    private LocalDateTime validFrom;
    
    @Column(name = "valid_until")
    private LocalDateTime validUntil;
    
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.ACTIVE;
    
    @Column(name = "center_lat")
    private Double centerLat;
    
    @Column(name = "center_lng")
    private Double centerLng;
    
    @Column(name = "radius_meters")
    private Integer radiusMeters = 2000;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public Ticket() {}
    
    public Ticket(String ticketCode, String touristId, String type,
                 LocalDateTime validFrom, LocalDateTime validUntil, 
                 Double centerLat, Double centerLng, Integer radiusMeters) {
        this.ticketCode = ticketCode;
        this.touristId = touristId;
        this.type = type;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.centerLat = centerLat;
        this.centerLng = centerLng;
        this.radiusMeters = radiusMeters;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }
    
    public String getTouristId() { return touristId; }
    public void setTouristId(String touristId) { this.touristId = touristId; }
    
    public String getType() { return type; }
    public void setDestination(String destination) { this.type = type; }
    
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
    
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
    
    public Double getCenterLat() { return centerLat; }
    public void setCenterLat(Double centerLat) { this.centerLat = centerLat; }
    
    public Double getCenterLng() { return centerLng; }
    public void setCenterLng(Double centerLng) { this.centerLng = centerLng; }
    
    public Integer getRadiusMeters() { return radiusMeters; }
    public void setRadiusMeters(Integer radiusMeters) { this.radiusMeters = radiusMeters; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public enum TicketStatus {
        ACTIVE, EXPIRED, CANCELLED
    }
}

package com.tourist.safety.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_blocks")
public class AuditBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long blockIndex;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private String eventType;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String eventData;
    
    @Column(nullable = false)
    private String previousHash;
    
    @Column(nullable = false)
    private String hash;
    
    public AuditBlock() {}
    
    public AuditBlock(Long blockIndex, LocalDateTime timestamp, String eventType, 
                      String eventData, String previousHash, String hash) {
        this.blockIndex = blockIndex;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.eventData = eventData;
        this.previousHash = previousHash;
        this.hash = hash;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getBlockIndex() { return blockIndex; }
    public void setBlockIndex(Long blockIndex) { this.blockIndex = blockIndex; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }
    
    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }
    
    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
}

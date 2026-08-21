package com.tourist.safety.service;

import com.tourist.safety.entity.Tourist;
import com.tourist.safety.repository.TouristRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TouristService {
    
    @Autowired
    private TouristRepository touristRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Transactional
    public Tourist registerTourist(String name, String email, String phone, 
                                   String nationality, String emergencyContact) {
        // Generate unique tourist ID
        String touristId = generateTouristId();
        
        Tourist tourist = new Tourist(touristId, name, email, phone, nationality, emergencyContact);
        tourist = touristRepository.save(tourist);
        
        // Create audit block
        String eventData = String.format("{\"touristId\":\"%s\",\"name\":\"%s\",\"email\":\"%s\"}", 
            touristId, name, email);
        auditService.createAuditBlock("TOURIST_REGISTERED", eventData);
        
        return tourist;
    }
    
    public Optional<Tourist> getTouristById(String touristId) {
        return touristRepository.findByTouristId(touristId);
    }

    public Tourist getTouristByEmail(String email){
        return touristRepository.findByEmail(email);
    }
    public List<Tourist> getAllTourists() {
        return touristRepository.findAll();
    }
    
    private String generateTouristId() {
        String id;
        do {
            id = "TID-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (touristRepository.existsByTouristId(id));
        return id;
    }
}

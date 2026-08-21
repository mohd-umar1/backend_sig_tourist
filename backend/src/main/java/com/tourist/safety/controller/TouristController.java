package com.tourist.safety.controller;

import com.tourist.safety.entity.Tourist;
import com.tourist.safety.service.TouristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tourists")
@CrossOrigin(origins = "*")
public class TouristController {
    
    @Autowired
    private TouristService touristService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerTourist(@RequestBody Map<String, String> request) {
        try {
            Tourist tourist = touristService.registerTourist(
                request.get("name"),
                request.get("email"),
                request.get("phone"),
                request.get("nationality"),
                request.get("emergencyContact")
            );
            return ResponseEntity.ok(tourist);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{touristId}")
    public ResponseEntity<?> getTourist(@PathVariable String touristId) {
        return touristService.getTouristById(touristId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Tourist tourist = touristService.getTouristByEmail(request.get("email"));
        if (tourist == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }else{
            return ResponseEntity.ok(tourist);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Tourist>> getAllTourists() {
        return ResponseEntity.ok(touristService.getAllTourists());
    }
}

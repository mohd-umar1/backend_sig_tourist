package com.tourist.safety.controller;

import com.tourist.safety.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    @PostMapping("/check")
    public ResponseEntity<?> checkLocation(@RequestBody Map<String, Object> request) {
        try {
            LocationService.LocationCheckResult result = locationService.checkLocation(
                (String) request.get("ticketCode"),
                request.get("latitude") != null ? ((Number) request.get("latitude")).doubleValue() : null,
                request.get("longitude") != null ? ((Number) request.get("longitude")).doubleValue() : null
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

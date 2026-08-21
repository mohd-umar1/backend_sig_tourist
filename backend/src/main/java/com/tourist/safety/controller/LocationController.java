package com.tourist.safety.controller;

import com.tourist.safety.entity.Incident;
import com.tourist.safety.entity.Restricted_zone;
import com.tourist.safety.entity.Tourist;
import com.tourist.safety.repository.RestrictedZoneRepo;
import com.tourist.safety.service.AuditService;
import com.tourist.safety.service.GeoFenceService;
import com.tourist.safety.service.IncidentService;
import com.tourist.safety.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {
    
    @Autowired
    private LocationService locationService;

    @Autowired
    private GeoFenceService geoFenceService;

    @Autowired
    private RestrictedZoneRepo restrictedZoneRepo;

    @Autowired
    private IncidentService incidentService;
    
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
    @PostMapping("add-restricted-zone")
    public ResponseEntity<?> addRestrictedZone(@RequestBody Map<String, Object> request) {
       try {
           Restricted_zone restricted_zone = new Restricted_zone(
                   request.get("name").toString(),
                   request.get("description").toString(),
                   ((Number) request.get("latitude")).doubleValue(),
                   ((Number) request.get("longitude")).doubleValue()
           );
           geoFenceService.addRestricted_zone(restricted_zone);
           return ResponseEntity.ok(restricted_zone);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
    }
    @PostMapping("/geofence")
    public ResponseEntity<?> geofence(@RequestBody Map<String, Object> request, Tourist tourist) {
        try{
            String tourist_id = (String)request.get("touristId");
            Double longitude = request.get("longitude") != null ? ((Number) request.get("longitude")).doubleValue() : null;
            Double latitude = request.get("latitude") !=null? ((Number) request.get("latitude")).doubleValue():null;
            List<Restricted_zone> restrictedZones = restrictedZoneRepo.findAll();
            for(Restricted_zone restrictedZone : restrictedZones) {
                if(geoFenceService.Check_transpass(restrictedZone,latitude,longitude)){
                    incidentService.createIncident(
                            tourist_id,
                            Incident.IncidentType.valueOf("GEOFENCE_VIOLATION"),
                            Incident.Severity.HIGH,
                            latitude,
                            longitude,
                            "tourist has transpassed into restricted location"
                    );
                    return ResponseEntity.ok(Map.of("violation", true,
                            "message", "Tourist entered a restricted zone"));
                }
            };
            return  ResponseEntity.ok(Map.of("violation", false,
                    "message", "Tourist not entered a restricted zone"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

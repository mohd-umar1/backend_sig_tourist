package com.tourist.safety.controller;

import com.tourist.safety.entity.AuditBlock;
import com.tourist.safety.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit-chain")
@CrossOrigin(origins = "*")
public class AuditController {
    
    @Autowired
    private AuditService auditService;
    
    @GetMapping
    public ResponseEntity<List<AuditBlock>> getAuditChain() {
        return ResponseEntity.ok(auditService.getAuditChain());
    }
    
    @GetMapping("/verify")
    public ResponseEntity<?> verifyChain() {
        AuditService.ChainVerificationResult result = auditService.verifyChain();
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/tamper")
    public ResponseEntity<?> tamperWithBlock(@RequestBody Map<String, Object> request) {
        try {
            Long blockIndex = ((Number) request.get("blockIndex")).longValue();
            AuditBlock block = auditService.tamperWithBlock(blockIndex);
            if (block == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(Map.of(
                "message", "Block " + blockIndex + " has been tampered with",
                "block", block
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/reversetamper")
    public ResponseEntity<?> reversetamper(@RequestBody Map<String, Object> request) {
        try {
            Long blockIndex = ((Number) request.get("blockIndex")).longValue();
            AuditBlock block = auditService.reverseTamper(blockIndex);
            if (block == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(Map.of(
                    "message", "Block " + blockIndex + " has been restored"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

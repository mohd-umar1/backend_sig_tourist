package com.tourist.safety.repository;

import com.tourist.safety.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByTouristId(String touristId);
    List<Incident> findByStatus(Incident.IncidentStatus status);
    List<Incident> findByType(Incident.IncidentType type);
    List<Incident> findByOrderByCreatedAtDesc();
}

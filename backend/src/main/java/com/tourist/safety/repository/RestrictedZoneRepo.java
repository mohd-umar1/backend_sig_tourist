package com.tourist.safety.repository;

import com.tourist.safety.entity.Restricted_zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestrictedZoneRepo extends JpaRepository<Restricted_zone,Integer> {
    Restricted_zone findByName(String name);
    Restricted_zone findById(int id);
}

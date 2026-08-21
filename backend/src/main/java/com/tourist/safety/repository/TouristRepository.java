package com.tourist.safety.repository;

import com.tourist.safety.entity.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long> {
    Optional<Tourist> findByTouristId(String touristId);
    Tourist findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTouristId(String touristId);
}

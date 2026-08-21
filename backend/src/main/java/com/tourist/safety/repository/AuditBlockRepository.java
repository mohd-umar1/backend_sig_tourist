package com.tourist.safety.repository;

import com.tourist.safety.entity.AuditBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditBlockRepository extends JpaRepository<AuditBlock, Long> {
    Optional<AuditBlock> findTopByOrderByBlockIndexDesc();
    List<AuditBlock> findAllByOrderByBlockIndexAsc();
    AuditBlock findByBlockIndex(Long blockIndex);
}

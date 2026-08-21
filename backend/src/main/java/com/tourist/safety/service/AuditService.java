package com.tourist.safety.service;

import com.tourist.safety.entity.AuditBlock;
import com.tourist.safety.repository.AuditBlockRepository;
import com.tourist.safety.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuditService {

    @Autowired
    private AuditBlockRepository auditBlockRepository;

    @Transactional
    public AuditBlock createAuditBlock(String eventType, String eventData) {
        Long blockIndex = (long) auditBlockRepository.count() + 1;
        String previousHash = getPreviousHash();
        LocalDateTime timestamp = LocalDateTime.now().withNano(0);

        String hash = HashUtil.calculateBlockHash(blockIndex, timestamp.toString(), eventType, eventData, previousHash);

        AuditBlock block = new AuditBlock(blockIndex, timestamp, eventType, eventData, previousHash, hash);
        return auditBlockRepository.save(block);
    }

    private String getPreviousHash() {
        long count = auditBlockRepository.count();
        if (count == 0) {
            return "0"; // Genesis block
        }
        Optional<AuditBlock> lastBlock = auditBlockRepository.findTopByOrderByBlockIndexDesc();
        return lastBlock.map(AuditBlock::getHash).orElse("0");
    }

    public List<AuditBlock> getAuditChain() {
        return auditBlockRepository.findAllByOrderByBlockIndexAsc();
    }

    public ChainVerificationResult verifyChain() {
        List<AuditBlock> chain = getAuditChain();

        if (chain.isEmpty()) {
            return new ChainVerificationResult(true, 0L, "Chain is empty");
        }

        // Verify genesis block
        AuditBlock genesis = chain.get(0);
        if (!genesis.getPreviousHash().equals("0")) {
            return new ChainVerificationResult(false, 0L, "Genesis block has invalid previous hash");
        }

        // Verify each block
        for (int i = 0; i < chain.size(); i++) {
            AuditBlock currentBlock = chain.get(i);

            // Recalculate hash
            String timestamp = currentBlock.getTimestamp().toString();
            String calculatedHash = HashUtil.calculateBlockHash(
                    currentBlock.getBlockIndex(),
                    timestamp,
                    currentBlock.getEventType(),
                    currentBlock.getEventData(),
                    currentBlock.getPreviousHash()
            );

            if (!calculatedHash.equals(currentBlock.getHash())) {
                return new ChainVerificationResult(false, currentBlock.getBlockIndex(),
                        "Hash mismatch at block " + currentBlock.getBlockIndex());
            }

            // Verify chain link (except genesis)
            if (i > 0) {
                AuditBlock previousBlock = chain.get(i - 1);
                if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                    return new ChainVerificationResult(false, currentBlock.getBlockIndex(),
                            "Chain link broken at block " + currentBlock.getBlockIndex());
                }
            }
        }

        return new ChainVerificationResult(true, -1L, "Chain is valid");
    }

    @Transactional
    public AuditBlock tamperWithBlock(Long blockIndex) {
        AuditBlock block = auditBlockRepository.findByBlockIndex(blockIndex);
        if (block != null && !block.getEventData().startsWith("TAMPERED: ")) {
            block.setEventData("TAMPERED: " + block.getEventData());
            return auditBlockRepository.save(block);
        }
        return null;
    }
    @Transactional
    public AuditBlock reverseTamper(Long blockIndex) {
        AuditBlock block = auditBlockRepository.findByBlockIndex(blockIndex);
        if(block!=null){
            block.setEventData(block.getEventData().substring(10));
        return auditBlockRepository.save(block);
        }
        return null;
    }

    public static class ChainVerificationResult {
        private boolean valid;
        private Long invalidBlockIndex;
        private String message;

        public ChainVerificationResult(boolean valid, Long invalidBlockIndex, String message) {
            this.valid = valid;
            this.invalidBlockIndex = invalidBlockIndex;
            this.message = message;
        }

        // Getters
        public boolean isValid() { return valid; }
        public Long getInvalidBlockIndex() { return invalidBlockIndex; }
        public String getMessage() { return message; }
    }
}

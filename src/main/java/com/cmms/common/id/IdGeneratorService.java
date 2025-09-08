package com.cmms.common.id;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Service
@RequiredArgsConstructor
public class IdGeneratorService {

    private final IdSequenceRepository idSequenceRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateId(String companyId, String prefix) {
        IdSequence sequence = idSequenceRepository.findByIdWithLock(companyId, prefix)
                .orElseThrow(() -> new RuntimeException("ID Sequence not found for prefix: " + prefix));

        long nextVal = sequence.getNextVal();
        sequence.setNextVal(nextVal + 1);
        idSequenceRepository.save(sequence);

        return String.format("%010d", nextVal);
    }

    // Special case for file group and file IDs as per docs
    public String generateFileGroupId() {
        long currentTimeMillis = System.currentTimeMillis();
        return "FG" + String.format("%08d", currentTimeMillis % 100000000L);
    }

    public String generateFileId() {
        return "FI" + String.format("%08d", System.currentTimeMillis() % 100000000L);
    }

    public String generateApprovalId() {
        return "WF" + String.format("%08d", System.currentTimeMillis() % 100000000L);
    }

    public String generateTemplateId() {
        return "WT" + String.format("%08d", System.currentTimeMillis() % 100000000L);
    }
}

package com.yulslab.cmms.common;

import com.yulslab.domain.sequence.IdSequence;
import com.yulslab.domain.sequence.IdSequenceId;
import com.yulslab.domain.sequence.IdSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdGenerationService {

    private final IdSequenceRepository idSequenceRepository;

    @Value("${app.default-company-code}")
    private String defaultCompanyId;

    @Transactional
    public String generateNextId(char prefix) {
        IdSequenceId id = new IdSequenceId();
        id.setCompanyId(defaultCompanyId);
        id.setPrefix(prefix);

        IdSequence sequence = idSequenceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID Sequence not found for prefix: " + prefix));

        long nextVal = sequence.getNextVal();
        sequence.setNextVal(nextVal + 1);
        idSequenceRepository.save(sequence);

        return String.valueOf(nextVal);
    }
}

package com.yulslab.cmms.inspection.service;

import com.yulslab.cmms.common.IdGenerationService;
import com.yulslab.cmms.inspection.entity.Inspection;
import com.yulslab.cmms.inspection.entity.InspectionId;
import com.yulslab.cmms.inspection.repository.InspectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final IdGenerationService idGenerationService;

    public List<Inspection> findInspections() {
        return inspectionRepository.findAll();
    }

    public Optional<Inspection> findById(InspectionId inspectionId) {
        return inspectionRepository.findById(inspectionId);
    }

    @Transactional
    public Inspection saveInspection(Inspection inspection) {
        if (inspection.getId() == null || inspection.getId().getInspectionId() == null || inspection.getId().getInspectionId().isEmpty()) {
            String nextId = idGenerationService.generateNextId('3'); // '3' for inspection
            if (inspection.getId() == null) {
                inspection.setId(new InspectionId());
            }
            inspection.getId().setInspectionId(nextId);
        }
        return inspectionRepository.save(inspection);
    }

    @Transactional
    public void deleteInspection(InspectionId inspectionId) {
        inspectionRepository.deleteById(inspectionId);
    }
}

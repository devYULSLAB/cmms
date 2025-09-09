package com.cmms.inspection.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.inspection.entity.Inspection;
import com.cmms.inspection.entity.InspectionId;
import com.cmms.inspection.entity.InspectionItem;
import com.cmms.inspection.repository.InspectionItemRepository;
import com.cmms.inspection.repository.InspectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InspectionService {

    private final InspectionRepository inspectionRepository;
    private final InspectionItemRepository inspectionItemRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional(readOnly = true)
    public Page<Inspection> getInspectionsByCompanyId(String companyId, Pageable pageable) {
        return inspectionRepository.findInspectionsByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public Inspection getInspectionById(InspectionId inspectionId) {
        return inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new RuntimeException("Inspection not found: " + inspectionId));
    }

    @Transactional
    public Inspection saveInspection(Inspection inspection) {
        if (inspection.getInspectionId() == null || inspection.getInspectionId().isEmpty()) {
            inspection.setInspectionId(idGeneratorService.generateId(inspection.getCompanyId(), "3"));
        }

        List<InspectionItem> items = inspection.getItems();
        if (items != null && !items.isEmpty()) {
            List<InspectionItem> validItems = new ArrayList<>();
            int itemIndex = 1;
            for (InspectionItem item : items) {
                if (item.getItemName() != null && !item.getItemName().isEmpty()) {
                    item.setCompanyId(inspection.getCompanyId());
                    item.setInspectionId(inspection.getInspectionId());
                    item.setItemId(String.format("%02d", itemIndex++));
                    item.setInspection(inspection);
                    validItems.add(item);
                }
            }
            items.clear();
            items.addAll(validItems);
        }

        return inspectionRepository.save(inspection);
    }

    @Transactional
    public void deleteInspection(InspectionId inspectionId) {
        if (!inspectionRepository.existsById(inspectionId)) {
            throw new RuntimeException("Inspection not found with ID: " + inspectionId);
        }
        inspectionItemRepository.deleteByCompanyIdAndInspectionId(inspectionId.getCompanyId(), inspectionId.getInspectionId());
        inspectionRepository.deleteById(inspectionId);
    }

    @Transactional(readOnly = true)
    public List<InspectionItem> getInspectionItemsByCompanyAndInspectionId(String companyId, String inspectionId) {
        return inspectionItemRepository.findByCompanyIdAndInspectionIdOrderByItemIdAsc(companyId, inspectionId);
    }

    @Transactional(readOnly = true)
    public List<Inspection> getRecentInspectionsByPlant(String companyId, String plantId) {
        return inspectionRepository.findTop5ByCompanyIdAndPlantIdOrderByPlannedDateDesc(companyId, plantId);
    }
}

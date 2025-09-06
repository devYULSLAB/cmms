package com.cmms.workpermit.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.workpermit.entity.Workpermit;
import com.cmms.workpermit.entity.WorkpermitId;
import com.cmms.workpermit.entity.WorkpermitItem;
import com.cmms.workpermit.entity.WorkpermitItemId;
import com.cmms.workpermit.repository.WorkpermitItemRepository;
import com.cmms.workpermit.repository.WorkpermitRepository;
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
public class WorkpermitService {

    private final WorkpermitRepository workpermitRepository;
    private final WorkpermitItemRepository workpermitItemRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional(readOnly = true)
    public Page<Workpermit> findWorkpermits(String companyId, Pageable pageable) {
        return workpermitRepository.findByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public Workpermit findWorkpermitById(WorkpermitId workpermitId) {
        return workpermitRepository.findById(workpermitId)
                .orElseThrow(() -> new RuntimeException("Workpermit not found: " + workpermitId));
    }

    @Transactional
    public Workpermit saveWorkpermit(Workpermit workpermit) {
        if (workpermit.getPermitId() == null || workpermit.getPermitId().isEmpty()) {
            workpermit.setPermitId(idGeneratorService.generateId(workpermit.getCompanyId(), "5"));
        }

        List<WorkpermitItem> items = workpermit.getItems();
        if (items != null && !items.isEmpty()) {
            List<WorkpermitItem> validItems = new ArrayList<>();
            int itemIndex = 1;
            for (WorkpermitItem item : items) {
                if (item.getItemName() != null && !item.getItemName().isEmpty()) {
                    item.setCompanyId(workpermit.getCompanyId());
                    item.setPermitId(workpermit.getPermitId());
                    item.setItemId(String.format("%02d", itemIndex++));
                    item.setWorkpermit(workpermit);
                    validItems.add(item);
                }
            }
            items.clear();
            items.addAll(validItems);
        }

        return workpermitRepository.save(workpermit);
    }

    @Transactional
    public void deleteWorkpermit(WorkpermitId workpermitId) {
        if (!workpermitRepository.existsById(workpermitId)) {
            throw new RuntimeException("Workpermit not found with ID: " + workpermitId);
        }
        workpermitItemRepository.deleteAll(findWorkpermitItems(workpermitId.getCompanyId(), workpermitId.getPermitId()));
        workpermitRepository.deleteById(workpermitId);
    }

    @Transactional(readOnly = true)
    public List<WorkpermitItem> findWorkpermitItems(String companyId, String permitId) {
        return workpermitItemRepository.findByCompanyIdAndPermitId(companyId, permitId);
    }

    @Transactional
    public void deleteWorkpermitItem(WorkpermitItemId itemId) {
        workpermitItemRepository.deleteById(itemId);
    }
}

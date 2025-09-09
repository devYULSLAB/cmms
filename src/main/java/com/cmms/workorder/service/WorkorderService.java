package com.cmms.workorder.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.workorder.entity.Workorder;
import com.cmms.workorder.entity.WorkorderId;
import com.cmms.workorder.entity.WorkorderItem;
import com.cmms.workorder.entity.WorkorderItemId;
import com.cmms.workorder.repository.WorkorderItemRepository;
import com.cmms.workorder.repository.WorkorderRepository;
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
public class WorkorderService {
    private final WorkorderRepository workorderRepository;
    private final WorkorderItemRepository workorderItemRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional(readOnly = true)
    public Page<Workorder> getWorkordersByCompanyId(String companyId, Pageable pageable) {
        return workorderRepository.findWorkordersByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public Workorder getWorkorderById(WorkorderId workorderId) {
        return workorderRepository.findById(workorderId)
                .orElseThrow(() -> new RuntimeException("Workorder not found: " + workorderId));
    }

    @Transactional
    public Workorder saveWorkorder(Workorder workorder) {
        if (workorder.getWorkorderId() == null || workorder.getWorkorderId().isEmpty()) {
            workorder.setWorkorderId(idGeneratorService.generateId(workorder.getCompanyId(), "5"));
        }

        List<WorkorderItem> items = workorder.getItems();
        if (items != null && !items.isEmpty()) {
            List<WorkorderItem> validItems = new ArrayList<>();
            int itemIndex = 1;
            for (WorkorderItem item : items) {
                if (item.getItemName() != null && !item.getItemName().isEmpty()) {
                    item.setCompanyId(workorder.getCompanyId());
                    item.setWorkorderId(workorder.getWorkorderId());
                    item.setItemId(String.format("%02d", itemIndex++));
                    item.setWorkorder(workorder);
                    validItems.add(item);
                }
            }
            items.clear();
            items.addAll(validItems);
        }

        return workorderRepository.save(workorder);
    }

    @Transactional
    public void deleteWorkorder(WorkorderId workorderId) {
        if (!workorderRepository.existsById(workorderId)) {
            throw new RuntimeException("Workorder not found with ID: " + workorderId);
        }
        workorderItemRepository.deleteAll(getWorkorderItemsByCompanyAndWorkorderId(workorderId.getCompanyId(), workorderId.getWorkorderId()));
        workorderRepository.deleteById(workorderId);
    }

    @Transactional(readOnly = true)
    public List<WorkorderItem> getWorkorderItemsByCompanyAndWorkorderId(String companyId, String workorderId) {
        return workorderItemRepository.findByCompanyIdAndWorkorderIdOrderByItemIdAsc(companyId, workorderId);
    }

    @Transactional
    public void deleteWorkorderItem(WorkorderItemId itemId) {
        workorderItemRepository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    public List<Workorder> getRecentWorkordersByPlant(String companyId, String plantId) {
        return workorderRepository.findTop5ByCompanyIdAndPlantIdOrderByPlannedDateDesc(companyId, plantId);
    }
}
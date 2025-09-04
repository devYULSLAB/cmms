package com.yulslab.cmms.workorder.service;

import com.yulslab.cmms.common.IdGenerationService;
import com.yulslab.cmms.workorder.entity.Workorder;
import com.yulslab.cmms.workorder.entity.WorkorderId;
import com.yulslab.cmms.workorder.repository.WorkorderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkorderService {
    private final WorkorderRepository workorderRepository;
    private final IdGenerationService idGenerationService;

    public List<Workorder> findWorkorders() {
        return workorderRepository.findAll();
    }

    public Optional<Workorder> findById(WorkorderId workorderId) {
        return workorderRepository.findById(workorderId);
    }

    @Transactional
    public Workorder saveWorkorder(Workorder workorder) {
        if (workorder.getId() == null || workorder.getId().getWorkorderId() == null || workorder.getId().getWorkorderId().isEmpty()) {
            String nextId = idGenerationService.generateNextId('5'); // '5' for workorder
            if (workorder.getId() == null) {
                workorder.setId(new WorkorderId());
            }
            workorder.getId().setWorkorderId(nextId);
        }
        return workorderRepository.save(workorder);
    }

    @Transactional
    public void deleteWorkorder(WorkorderId workorderId) {
        workorderRepository.deleteById(workorderId);
    }
}

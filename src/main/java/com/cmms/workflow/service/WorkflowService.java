package com.cmms.workflow.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.workflow.entity.*;
import com.cmms.workflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkflowService {

    private final ApprovalTemplateRepository templateRepository;
    private final ApprovalTemplateStepRepository templateStepRepository;
    private final ApprovalRequestRepository requestRepository;
    private final ApprovalRequestStepRepository requestStepRepository;
    private final IdGeneratorService idGeneratorService;

    // == Template Methods ==
    @Transactional(readOnly = true)
    public Page<ApprovalTemplate> findTemplates(String companyId, Pageable pageable) {
        return templateRepository.findByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public ApprovalTemplate findTemplateById(ApprovalTemplateId id) {
        return templateRepository.findById(id).orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public ApprovalTemplate saveTemplate(ApprovalTemplate template) {
        if (template.getId() == null || template.getId().isEmpty()) {
            template.setId(idGeneratorService.generateId(template.getCompanyId(), "W"));
        }
        // TODO: Handle steps saving logic
        return templateRepository.save(template);
    }

    public void deleteTemplate(ApprovalTemplateId id) {
        templateRepository.deleteById(id);
    }

    // == Request Methods ==
    @Transactional(readOnly = true)
    public Page<ApprovalRequest> findRequests(String companyId, Pageable pageable) {
        return requestRepository.findByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public ApprovalRequest findRequestById(ApprovalRequestId id) {
        return requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public ApprovalRequest saveRequest(ApprovalRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            request.setId(idGeneratorService.generateId(request.getCompanyId(), "W"));
        }
        // TODO: Handle steps creation from template
        return requestRepository.save(request);
    }

    public void deleteRequest(ApprovalRequestId id) {
        requestRepository.deleteById(id);
    }
}

package com.cmms.workflow.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.workflow.entity.*;
import com.cmms.workflow.repository.ApprovalTemplateRepository;
import com.cmms.workflow.repository.ApprovalRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkflowService {

    private final ApprovalTemplateRepository templateRepository;
    private final ApprovalRequestRepository requestRepository;
    private final IdGeneratorService idGeneratorService;

    // == Template Methods ==
    @Transactional(readOnly = true)
    public Page<ApprovalTemplate> getTemplatesByCompanyId(String companyId, Pageable pageable) {
        return templateRepository.findTemplatesByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public ApprovalTemplate getTemplateById(ApprovalTemplateId id) {
        return templateRepository.findById(id).orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public ApprovalTemplate saveTemplate(ApprovalTemplate template) {
        if (template.getTemplateId() == null || template.getTemplateId().isEmpty()) {
            template.setTemplateId(idGeneratorService.generateTemplateId());
        }
        // TODO: Handle steps saving logic
        return templateRepository.save(template);
    }

    public void deleteTemplate(ApprovalTemplateId id) {
        templateRepository.deleteById(id);
    }

    // == Request Methods ==
    @Transactional(readOnly = true)
    public Page<ApprovalRequest> getRequestsByCompanyId(String companyId, Pageable pageable) {
        return requestRepository.findRequestsByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public ApprovalRequest getRequestById(ApprovalRequestId id) {
        return requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    public ApprovalRequest saveRequest(ApprovalRequest request) {
        if (request.getApprovalId() == null || request.getApprovalId().isEmpty()) {
            request.setApprovalId(idGeneratorService.generateApprovalId());
        }
        // TODO: Handle steps creation from template
        return requestRepository.save(request);
    }

    public void deleteRequest(ApprovalRequestId id) {
        requestRepository.deleteById(id);
    }
}

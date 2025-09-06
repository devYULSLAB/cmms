package com.cmms.workflow.repository;

import com.cmms.workflow.entity.ApprovalTemplateStep;
import com.cmms.workflow.entity.ApprovalTemplateStepId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalTemplateStepRepository extends JpaRepository<ApprovalTemplateStep, ApprovalTemplateStepId> {
    List<ApprovalTemplateStep> findByCompanyIdAndTemplateIdOrderBySortOrderAsc(String companyId, String templateId);
}

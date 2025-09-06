package com.cmms.workflow.repository;

import com.cmms.workflow.entity.ApprovalRequestStep;
import com.cmms.workflow.entity.ApprovalRequestStepId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRequestStepRepository extends JpaRepository<ApprovalRequestStep, ApprovalRequestStepId> {
    List<ApprovalRequestStep> findByCompanyIdAndApprovalIdOrderBySortOrderAsc(String companyId, String approvalId);
}

package com.cmms.workflow.repository;

import com.cmms.workflow.entity.ApprovalTemplate;
import com.cmms.workflow.entity.ApprovalTemplateId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalTemplateRepository extends JpaRepository<ApprovalTemplate, ApprovalTemplateId> {
    Page<ApprovalTemplate> findByCompanyId(String companyId, Pageable pageable);
}

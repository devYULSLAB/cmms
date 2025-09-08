package com.cmms.workflow.repository;

import com.cmms.workflow.entity.ApprovalRequest;
import com.cmms.workflow.entity.ApprovalRequestId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, ApprovalRequestId> {
    Page<ApprovalRequest> findRequestsByCompanyId(String companyId, Pageable pageable);
}

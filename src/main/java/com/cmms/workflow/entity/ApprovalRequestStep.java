package com.cmms.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "approval_request_step")
@IdClass(ApprovalRequestStepId.class)
@DynamicInsert
@DynamicUpdate
public class ApprovalRequestStep {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "approval_id", length = 10, nullable = false)
    private String approvalId;

    @Id
    @Column(name = "step_no", nullable = false)
    private Integer stepNo;

    @Column(name = "approver_user", length = 5, nullable = false)
    private String approverUser;

    @Column(name = "decision", length = 1, nullable = false)
    private String decision;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "approval_id", referencedColumnName = "approval_id", insertable = false, updatable = false)
    })
    private ApprovalRequest approvalRequest;
}

package com.cmms.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "approval_template_step")
@IdClass(ApprovalTemplateStepId.class)
@DynamicInsert
@DynamicUpdate
public class ApprovalTemplateStep {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "template_id", length = 10, nullable = false)
    private String templateId;

    @Id
    @Column(name = "step_no", nullable = false)
    private Integer stepNo;

    @Column(name = "role_id", length = 5)
    private String roleId;

    @Column(name = "approver_user", length = 5)
    private String approverUser;

    @Column(name = "condition_expr", length = 200)
    private String conditionExpr;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "template_id", referencedColumnName = "template_id", insertable = false, updatable = false)
    })
    private ApprovalTemplate approvalTemplate;
}

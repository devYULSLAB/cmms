package com.cmms.workflow.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "approval_template")
@IdClass(ApprovalTemplateId.class)
@DynamicInsert
@DynamicUpdate
public class ApprovalTemplate {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "template_id", length = 10, nullable = false)
    private String templateId;

    @Column(name = "template_name", length = 100, nullable = false)
    private String templateName;

    @Column(name = "description", length = 300)
    private String description;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "approvalTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ApprovalTemplateStep> steps = new ArrayList<>();
}

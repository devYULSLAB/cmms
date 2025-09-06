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
@Table(name = "approval_request")
@IdClass(ApprovalRequestId.class)
@DynamicInsert
@DynamicUpdate
public class ApprovalRequest {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "approval_id", length = 10, nullable = false)
    private String approvalId;

    @Column(name = "template_id", length = 10)
    private String templateId;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "requester_user", length = 5, nullable = false)
    private String requesterUser;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "file_group_id", length = 10)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "approvalRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ApprovalRequestStep> steps = new ArrayList<>();
}

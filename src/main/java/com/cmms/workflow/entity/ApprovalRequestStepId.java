package com.cmms.workflow.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestStepId implements Serializable {
    private String companyId;
    private String approvalId;
    private Integer stepNo;
}

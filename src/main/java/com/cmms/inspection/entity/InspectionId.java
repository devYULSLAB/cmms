package com.cmms.inspection.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionId implements Serializable {
    private String companyId;
    private String inspectionId;
}
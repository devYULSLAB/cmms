package com.yulslab.cmms.inspection.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class InspectionItemId implements Serializable {
    private String companyId;
    private String siteId;
    private String inspectionId;
    private String itemId;
}

package com.yulslab.cmms.workorder.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class WorkorderItemId implements Serializable {
    private String companyId;
    private String siteId;
    private String workorderId;
    private String itemId;
}

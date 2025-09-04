package com.yulslab.cmms.inspection.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "inspection_item")
public class InspectionItem {

    @EmbeddedId
    private InspectionItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumns({
        @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
        @JoinColumn(name = "site_id", referencedColumnName = "site_id", insertable = false, updatable = false),
        @JoinColumn(name = "inspection_id", referencedColumnName = "inspection_id", insertable = false, updatable = false)
    })
    private Inspection inspection;

    private String itemName;
    private String method;
    private String unit;
    private BigDecimal minVal;
    private BigDecimal maxVal;
    private BigDecimal stdVal;
    private BigDecimal resultVal;
    private String notes;
}

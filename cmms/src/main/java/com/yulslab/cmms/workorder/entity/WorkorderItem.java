package com.yulslab.cmms.workorder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "workorder_item")
public class WorkorderItem {

    @EmbeddedId
    private WorkorderItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumns({
        @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
        @JoinColumn(name = "site_id", referencedColumnName = "site_id", insertable = false, updatable = false),
        @JoinColumn(name = "workorder_id", referencedColumnName = "workorder_id", insertable = false, updatable = false)
    })
    private Workorder workorder;

    private String itemName;
    private String method;
    private String resultValue;
    private String partInventoryId;
    private BigDecimal qty;
    private String uom;
}

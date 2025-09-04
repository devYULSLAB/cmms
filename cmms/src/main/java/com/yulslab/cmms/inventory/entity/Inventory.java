package com.yulslab.cmms.inventory.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class Inventory {

    @EmbeddedId
    private InventoryId id;

    private String inventoryName;
    private String masterType;
    private String deptId;
    private String makerName;
    private String spec;
    private String modelNo;
    private String serialNo;
    private String notes;
    private String fileGroupId;
}

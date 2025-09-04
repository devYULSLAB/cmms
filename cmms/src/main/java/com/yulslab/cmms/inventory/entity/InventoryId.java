package com.yulslab.cmms.inventory.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class InventoryId implements Serializable {
    private String companyId;
    private String inventoryId;
}

package com.cmms.inventory.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockByMonthId implements Serializable {
    private String companyId;
    private String siteId;
    private String storageId;
    private String inventoryId;
    private String yyyymm;
}

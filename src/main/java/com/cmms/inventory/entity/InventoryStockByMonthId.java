package com.cmms.inventory.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStockByMonthId implements Serializable {
    private String companyId;
    private String siteId;
    private String storageId;
    private String inventoryId;
    private String yyyymm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryStockByMonthId that = (InventoryStockByMonthId) o;
        return Objects.equals(companyId, that.companyId) &&
                Objects.equals(siteId, that.siteId) &&
                Objects.equals(storageId, that.storageId) &&
                Objects.equals(inventoryId, that.inventoryId) &&
                Objects.equals(yyyymm, that.yyyymm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, siteId, storageId, inventoryId, yyyymm);
    }
}

package com.cmms.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InventoryLedgerDto {
    private String inventoryId;
    private String itemName;
    private String storageId;
    private BigDecimal beginningQty;
    private BigDecimal receivedQty;
    private BigDecimal issuedQty;
    private BigDecimal adjustedQty;
    private BigDecimal endingQty;

    public InventoryLedgerDto(String inventoryId, String itemName, String storageId, BigDecimal beginningQty, BigDecimal receivedQty, BigDecimal issuedQty, BigDecimal adjustedQty, BigDecimal endingQty) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.storageId = storageId;
        this.beginningQty = beginningQty;
        this.receivedQty = receivedQty;
        this.issuedQty = issuedQty;
        this.adjustedQty = adjustedQty;
        this.endingQty = endingQty;
    }
}

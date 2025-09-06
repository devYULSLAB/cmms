package com.cmms.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_stock_by_month")
@IdClass(InventoryStockByMonthId.class)
public class InventoryStockByMonth {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "site_id", length = 5, nullable = false)
    private String siteId;

    @Id
    @Column(name = "storage_id", length = 5, nullable = false)
    private String storageId;

    @Id
    @Column(name = "inventory_id", length = 10, nullable = false)
    private String inventoryId;

    @Id
    @Column(name = "yyyymm", length = 6, nullable = false)
    private String yyyymm;

    @Column(name = "closing_qty", precision = 18, scale = 3, nullable = false)
    private BigDecimal closingQty;

    @Column(name = "closing_total_value", precision = 18, scale = 2)
    private BigDecimal closingTotalValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

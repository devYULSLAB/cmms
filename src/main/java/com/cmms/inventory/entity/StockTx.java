package com.cmms.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_tx")
@IdClass(StockTxId.class)
@DynamicInsert
@DynamicUpdate
public class StockTx {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "tx_id", length = 10, nullable = false)
    private String txId;

    @Column(name = "site_id", length = 5, nullable = false)
    private String siteId;

    @Column(name = "inventory_id", length = 10, nullable = false)
    private String inventoryId;

    @Column(name = "action_type", length = 10, nullable = false)
    private String actionType;

    @Column(name = "qty_change", precision = 18, scale = 3, nullable = false)
    private BigDecimal qtyChange;

    @Column(name = "unit_price", precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "amount_change", precision = 18, scale = 2)
    private BigDecimal amountChange;

    @Column(name = "src_storage_id", length = 5)
    private String srcStorageId;

    @Column(name = "dst_storage_id", length = 5)
    private String dstStorageId;

    @Column(name = "before_qty", precision = 18, scale = 3)
    private BigDecimal beforeQty;

    @Column(name = "after_qty", precision = 18, scale = 3)
    private BigDecimal afterQty;

    @Column(name = "related_type", length = 20)
    private String relatedType;

    @Column(name = "related_id", length = 10)
    private String relatedId;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;
}

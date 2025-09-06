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
@Table(name = "stock")
@IdClass(StockId.class)
@DynamicInsert
@DynamicUpdate
public class Stock {

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

    @Column(name = "qty", precision = 18, scale = 3, nullable = false)
    private BigDecimal qty;

    @Column(name = "unit_price", precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 18, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalAmount;

    @Column(name = "uom", length = 20)
    private String uom;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;
}
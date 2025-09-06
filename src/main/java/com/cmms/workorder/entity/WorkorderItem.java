package com.cmms.workorder.entity;

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
@Table(name = "workorder_item")
@IdClass(WorkorderItemIdClass.class)
@DynamicInsert
@DynamicUpdate
public class WorkorderItem {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "workorder_id", length = 10, nullable = false)
    private String workorderId;

    @Id
    @Column(name = "item_id", length = 2, nullable = false)
    private String itemId;

    @Column(name = "item_name", length = 100, nullable = false)
    private String itemName;

    @Column(name = "method", length = 100)
    private String method;

    @Column(name = "result_value", length = 100)
    private String resultValue;

    @Column(name = "part_inventory_id", length = 10)
    private String partInventoryId;

    @Column(name = "qty", precision = 18, scale = 3)
    private BigDecimal qty;

    @Column(name = "uom", length = 20)
    private String uom;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "workorder_id", referencedColumnName = "workorder_id", insertable = false, updatable = false)
    })
    private Workorder workorder;
}

package com.cmms.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory")
@IdClass(InventoryId.class)
@DynamicInsert
@DynamicUpdate
public class Inventory {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "inventory_id", length = 10, nullable = false)
    private String inventoryId;

    @Column(name = "inventory_name", length = 100, nullable = false)
    private String inventoryName;

    @Column(name = "master_type", length = 5)
    private String masterType;

    @Column(name = "dept_id", length = 5)
    private String deptId;

    @Column(name = "maker_name", length = 100)
    private String makerName;

    @Column(name = "spec", length = 100)
    private String spec;

    @Column(name = "model_no", length = 100)
    private String modelNo;

    @Column(name = "serial_no", length = 100)
    private String serialNo;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "file_group_id", length = 10)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}

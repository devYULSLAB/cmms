package com.cmms.inspection.entity;

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
@Table(name = "inspection_item")
@IdClass(InspectionItemIdClass.class)
@DynamicInsert
@DynamicUpdate
public class InspectionItem {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "inspection_id", length = 10, nullable = false)
    private String inspectionId;

    @Id
    @Column(name = "item_id", length = 2, nullable = false)
    private String itemId;

    @Column(name = "item_name", length = 100, nullable = false)
    private String itemName;

    @Column(name = "method", length = 100)
    private String method;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "min_val", precision = 18, scale = 4)
    private BigDecimal minVal;

    @Column(name = "max_val", precision = 18, scale = 4)
    private BigDecimal maxVal;

    @Column(name = "std_val", precision = 18, scale = 4)
    private BigDecimal stdVal;

    @Column(name = "result_val", precision = 18, scale = 4)
    private BigDecimal resultVal;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "inspection_id", referencedColumnName = "inspection_id", insertable = false, updatable = false)
    })
    private Inspection inspection;
}

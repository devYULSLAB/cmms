package com.cmms.workorder.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workorder")
@IdClass(WorkorderIdClass.class)
@DynamicInsert
@DynamicUpdate
public class Workorder {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "workorder_id", length = 10, nullable = false)
    private String workorderId;

    @Column(name = "workorder_name", length = 100, nullable = false)
    private String workorderName;

    @Column(name = "plant_id", length = 10)
    private String plantId;

    @Column(name = "job_type", length = 5)
    private String jobType;

    @Column(name = "site_id", length = 5)
    private String siteId;

    @Column(name = "planned_date")
    private LocalDate plannedDate;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "planned_cost", precision = 18, scale = 2)
    private BigDecimal plannedCost;

    @Column(name = "actual_cost", precision = 18, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "planned_time", precision = 18, scale = 2)
    private BigDecimal plannedTime;

    @Column(name = "actual_time", precision = 18, scale = 2)
    private BigDecimal actualTime;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "file_group_id", length = 10)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "workorder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WorkorderItem> items = new ArrayList<>();
}

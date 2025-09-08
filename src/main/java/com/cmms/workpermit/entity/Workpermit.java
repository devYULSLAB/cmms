package com.cmms.workpermit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workpermit")
@IdClass(WorkpermitId.class)
@DynamicInsert
@DynamicUpdate
public class Workpermit {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "permit_id", length = 10, nullable = false)
    private String permitId;

    @Column(name = "permit_name", length = 100, nullable = false)
    private String permitName;

    @Column(name = "permit_type", length = 5)
    private String permitType;

    @Column(name = "workorder_id", length = 10)
    private String workorderId;

    @Column(name = "plant_id", length = 10)
    private String plantId;

    @Column(name = "site_id", length = 5)
    private String siteId;

    @Column(name = "dept_id", length = 5)
    private String deptId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "work_summary", length = 100)
    private String workSummary;

    @Column(name = "hazard_factor", length = 100)
    private String hazardFactor;

    @Column(name = "safety_factor", length = 100)
    private String safetyFactor;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "file_group_id", length = 10)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "workpermit", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WorkpermitItem> items = new ArrayList<>();

}
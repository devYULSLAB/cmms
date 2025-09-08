package com.cmms.inspection.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inspection")
@IdClass(InspectionId.class)
@DynamicInsert
@DynamicUpdate
public class Inspection {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "inspection_id", length = 10, nullable = false)
    private String inspectionId;

    @Column(name = "inspection_name", length = 100, nullable = false)
    private String inspectionName;

    @Column(name = "plant_id", length = 10, nullable = false)
    private String plantId;

    @Column(name = "job_type", length = 5)
    private String jobType;

    @Column(name = "site_id", length = 5, nullable = false)
    private String siteId;

    @Column(name = "dept_id", length = 5)
    private String deptId;

    @Column(name = "planned_date")
    private LocalDateTime plannedDate;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "file_group_id", length = 10)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InspectionItem> items = new ArrayList<>();

}

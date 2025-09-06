package com.cmms.plant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plant")
@IdClass(PlantId.class)
@DynamicInsert
@DynamicUpdate
public class Plant {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "plant_id", length = 10, nullable = false)
    private String plantId;

    @Column(name = "plant_name", length = 100, nullable = false)
    private String plantName;

    @Column(name = "master_type", length = 5)
    private String masterType;

    @Column(name = "site_id", length = 5)
    private String siteId;

    @Column(name = "func_id", length = 5)
    private String funcId;

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

    @Column(name = "install_date")
    private LocalDate installDate;

    @Column(name = "depre_type", length = 5)
    private String depreType;

    @Column(name = "depre_period")
    private Integer deprePeriod;

    @Column(name = "acquire_cost", precision = 18, scale = 2)
    private BigDecimal acquireCost;

    @Column(name = "residual_value", precision = 18, scale = 2)
    private BigDecimal residualValue;

    @Column(name = "preventive_yn", length = 1, nullable = false)
    private String preventiveYn;

    @Column(name = "psm_yn", length = 1, nullable = false)
    private String psmYn;

    @Column(name = "wp_target_yn", length = 1, nullable = false)
    private String wpTargetYn;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "file_group_id", length = 10)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}

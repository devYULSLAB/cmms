package com.yulslab.cmms.plant.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "plant")
@Getter
@Setter
public class Plant {

    @EmbeddedId
    private PlantId id;

    private String plantName;
    private String masterType;
    private String funcId;
    private String deptId;
    private String makerName;
    private String spec;
    private String modelNo;
    private String serialNo;
    private LocalDate installDate;
    private String depreType;
    private Integer deprePeriod;
    private BigDecimal acquireCost;
    private BigDecimal residualValue;
    private char preventiveYn;
    private char psmYn;
    private char wpTargetYn;
    private String notes;
    private String fileGroupId;

    @CreationTimestamp
    private LocalDateTime createDate;

    @org.hibernate.annotations.UpdateTimestamp
    private java.time.LocalDateTime updateDate;
}

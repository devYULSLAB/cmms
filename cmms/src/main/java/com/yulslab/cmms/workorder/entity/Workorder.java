package com.yulslab.cmms.workorder.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "workorder")
public class Workorder {

    @EmbeddedId
    private WorkorderId id;

    private String workorderName;
    private String plantId;
    private String jobType;
    private LocalDate plannedDate;
    private LocalDate actualDate;
    private char status;
    private BigDecimal plannedCost;
    private BigDecimal actualCost;
    private BigDecimal plannedTime;
    private BigDecimal actualTime;
    private String notes;
    private String fileGroupId;

    @OneToMany(mappedBy = "workorder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WorkorderItem> items;
}

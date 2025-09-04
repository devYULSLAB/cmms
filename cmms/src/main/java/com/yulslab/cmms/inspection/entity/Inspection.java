package com.yulslab.cmms.inspection.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "inspection")
public class Inspection {

    @EmbeddedId
    private InspectionId id;

    private String inspectionName;
    private String plantId;
    private String jobType;
    private String deptId;
    private LocalDateTime plannedDate;
    private LocalDateTime actualDate;
    private char status;
    private String notes;
    private String fileGroupId;

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InspectionItem> items;
}

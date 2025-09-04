package com.yulslab.cmms.inspection.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InspectionDto {
    private String inspectionId;
    private String inspectionName;
    private String plantId;
    private LocalDateTime plannedDate;
    // Add other fields as needed
    private List<InspectionItemDto> items;
}

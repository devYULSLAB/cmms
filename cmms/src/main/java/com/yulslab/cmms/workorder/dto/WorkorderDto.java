package com.yulslab.cmms.workorder.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkorderDto {
    private String workorderId;
    private String workorderName;
    private String plantId;
    private LocalDate plannedDate;
    // Add other fields as needed
    private List<WorkorderItemDto> items;
}

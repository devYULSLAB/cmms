package com.yulslab.cmms.inspection.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class InspectionItemDto {
    private String itemId;
    private String itemName;
    private String method;
    private BigDecimal resultVal;
    // Add other fields as needed
}

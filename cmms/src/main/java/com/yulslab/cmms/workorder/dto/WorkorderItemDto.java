package com.yulslab.cmms.workorder.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class WorkorderItemDto {
    private String itemId;
    private String itemName;
    private String resultValue;
    private String partInventoryId;
    private BigDecimal qty;
    // Add other fields as needed
}

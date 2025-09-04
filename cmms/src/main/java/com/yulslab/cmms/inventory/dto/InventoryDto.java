package com.yulslab.cmms.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDto {
    private Long id;
    private String itemName;
    private int quantity;
    private String location;
}

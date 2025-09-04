package com.yulslab.cmms.inventory.controller;

import com.yulslab.cmms.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/inventoryList")
    public String inventoryList() {
        return "inventory/inventoryList";
    }

    @GetMapping("/inventoryForm")
    public String inventoryForm() {
        return "inventory/inventoryForm";
    }

    @GetMapping("/inventoryDetail")
    public String inventoryDetail() {
        return "inventory/inventoryDetail";
    }
}

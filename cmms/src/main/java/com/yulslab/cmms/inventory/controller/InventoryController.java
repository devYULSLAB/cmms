package com.yulslab.cmms.inventory.controller;

import com.yulslab.cmms.inventory.entity.Inventory;
import com.yulslab.cmms.inventory.entity.InventoryId;
import com.yulslab.cmms.inventory.service.InventoryService;
import com.yulslab.domain.common.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final DomainService domainService;

    @Value("${app.default-company-code}")
    private String defaultCompanyId;

    @GetMapping("/inventoryList")
    public String inventoryList(Model model) {
        List<Inventory> inventories = inventoryService.findInventories();
        model.addAttribute("inventories", inventories);
        return "inventory/inventoryList";
    }

    @GetMapping("/inventoryDetail")
    public String inventoryDetail(@RequestParam String companyId, @RequestParam String inventoryId, Model model) {
        InventoryId iId = new InventoryId();
        iId.setCompanyId(companyId);
        iId.setInventoryId(inventoryId);
        inventoryService.findById(iId).ifPresent(inventory -> model.addAttribute("inventory", inventory));
        return "inventory/inventoryDetail";
    }

    @GetMapping("/inventoryForm")
    public String inventoryForm(@RequestParam(required = false) String companyId,
                                @RequestParam(required = false) String inventoryId,
                                Model model) {
        if (inventoryId != null) {
            InventoryId iId = new InventoryId();
            iId.setCompanyId(companyId);
            iId.setInventoryId(inventoryId);
            inventoryService.findById(iId).ifPresent(inventory -> model.addAttribute("inventory", inventory));
        } else {
            model.addAttribute("inventory", new Inventory());
        }

        model.addAttribute("depts", domainService.getDepts(defaultCompanyId));
        model.addAttribute("assetTypes", domainService.getCodes(defaultCompanyId, "ASSET"));
        return "inventory/inventoryForm";
    }

    @PostMapping("/save")
    public String saveInventory(@ModelAttribute("inventory") Inventory inventory) {
        // For new entities, the companyId is not bound from the form, set it here.
        if (inventory.getId() != null && inventory.getId().getCompanyId() == null) {
            inventory.getId().setCompanyId(defaultCompanyId);
        }
        inventoryService.saveInventory(inventory);
        return "redirect:/inventory/inventoryList";
    }
}

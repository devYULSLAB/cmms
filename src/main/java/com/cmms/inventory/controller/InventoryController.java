package com.cmms.inventory.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.common.service.CommonCodeService;
import com.cmms.domain.service.DeptService;
import com.cmms.domain.service.SiteService;
import com.cmms.inventory.dto.InventoryTransactionForm;
import com.cmms.inventory.entity.Inventory;
import com.cmms.inventory.entity.InventoryId;
import com.cmms.inventory.entity.StockTx;
import com.cmms.inventory.service.InventoryService;
import com.cmms.inventory.service.StockService;
import com.cmms.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final StockService stockService;
    // Assuming other services will be refactored and injected
    // private final DeptService deptService;
    // private final SiteService siteService;
    // private final CommonCodeService commonCodeService;
    // private final StorageService storageService;

    // ========== Inventory Master CRUD ==========

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PageableDefault(size = 10, sort = "inventoryId") Pageable pageable) {
        Page<Inventory> inventories = inventoryService.findInventories(userDetails.getCompanyId(), pageable);
        model.addAttribute("inventories", inventories);
        return "inventory/list";
    }

    @GetMapping("/form")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("inventory", new Inventory());
        // Populate model with necessary data for select boxes
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("assetTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "ASSET"));
        return "inventory/form";
    }

    @GetMapping("/{inventoryId}/edit")
    public String editForm(@PathVariable String inventoryId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        InventoryId id = new InventoryId(userDetails.getCompanyId(), inventoryId);
        Inventory inventory = inventoryService.findInventoryById(id);
        model.addAttribute("inventory", inventory);
        // Populate model with necessary data for select boxes
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("assetTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "ASSET"));
        return "inventory/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Inventory inventory, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        inventory.setCompanyId(userDetails.getCompanyId());
        inventoryService.saveInventory(inventory);
        redirectAttributes.addFlashAttribute("message", "Inventory saved successfully.");
        return "redirect:/inventory/list";
    }

    @GetMapping("/{inventoryId}")
    public String detail(@PathVariable String inventoryId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        InventoryId id = new InventoryId(userDetails.getCompanyId(), inventoryId);
        model.addAttribute("inventory", inventoryService.findInventoryById(id));
        return "inventory/detail";
    }

    @PostMapping("/{inventoryId}/delete")
    public String delete(@PathVariable String inventoryId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        InventoryId id = new InventoryId(userDetails.getCompanyId(), inventoryId);
        try {
            inventoryService.deleteInventory(id);
            redirectAttributes.addFlashAttribute("message", "Inventory deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting inventory: " + e.getMessage());
        }
        return "redirect:/inventory/list";
    }

    // ========== Stock Transaction ==========

    @GetMapping("/transaction")
    public String transactionForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("form", new InventoryTransactionForm());
        // model.addAttribute("sites", siteService.findAllByCompanyId(userDetails.getCompanyId()));
        return "inventory/transaction";
    }

    @PostMapping("/transaction/save")
    public String saveTransaction(@RequestParam String actionType, @ModelAttribute InventoryTransactionForm form, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        List<StockTx> transactions = form.getStockTxList().stream()
                .filter(tx -> tx.getInventoryId() != null && !tx.getInventoryId().isEmpty() && tx.getQtyChange() != null)
                .peek(tx -> {
                    tx.setCompanyId(userDetails.getCompanyId());
                    tx.setSiteId(form.getSiteId());
                    tx.setActionType(actionType);
                    // tx.setCreatedAt(LocalDateTime.now()); // Service layer might handle this
                }).collect(Collectors.toList());

        if (transactions.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No valid transactions to save.");
            return "redirect:/inventory/transaction";
        }

        try {
            stockService.processStockTransaction(transactions);
            redirectAttributes.addFlashAttribute("successMessage", "Stock transaction processed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing transaction: " + e.getMessage());
        }

        return "redirect:/inventory/transaction";
    }

    // ========== API ==========

    @GetMapping("/api/details")
    @ResponseBody
    public Inventory getInventoryDetails(@RequestParam String inventoryId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        InventoryId id = new InventoryId(userDetails.getCompanyId(), inventoryId);
        try {
            return inventoryService.findInventoryById(id);
        } catch (Exception e) {
            return null;
        }
    }
}

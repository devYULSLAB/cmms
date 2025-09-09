package com.cmms.inventory.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.common.excel.ExcelService;
import com.cmms.domain.site.service.SiteService;
import com.cmms.inventory.dto.InventoryLedgerDto;
import com.cmms.inventory.dto.InventoryTransactionForm;
import com.cmms.inventory.entity.Inventory;
import com.cmms.inventory.entity.InventoryId;
import com.cmms.inventory.entity.StockTx;
import com.cmms.inventory.service.InventoryService;
import com.cmms.inventory.service.StockService;
import com.cmms.storage.service.StorageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final StockService stockService;
    private final SiteService siteService;
    private final StorageService storageService;
    private final ExcelService excelService;

    // ========== Inventory Master CRUD ==========

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PageableDefault(size = 10, sort = "inventoryId") Pageable pageable) {
        Page<Inventory> inventories = inventoryService.getInventoriesByCompanyId(userDetails.getCompanyId(), pageable);
        model.addAttribute("inventories", inventories);
        return "inventory/inventoryList";
    }

    @GetMapping("/form")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("inventory", new Inventory());
        // Populate model with necessary data for select boxes
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("assetTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "ASSET"));
        return "inventory/inventoryForm";
    }

    @GetMapping("/{inventoryId}/edit")
    public String editForm(@PathVariable String inventoryId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        InventoryId id = new InventoryId(userDetails.getCompanyId(), inventoryId);
        Inventory inventory = inventoryService.getInventoryById(id);
        model.addAttribute("inventory", inventory);
        // Populate model with necessary data for select boxes
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("assetTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "ASSET"));
        return "inventory/inventoryForm";
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
        model.addAttribute("inventory", inventoryService.getInventoryById(id));
        return "inventory/inventoryDetail";
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
        model.addAttribute("sites", siteService.getSitesByCompanyId(userDetails.getCompanyId()));
        return "inventory/inventoryTransaction";
    }

    @PostMapping("/stock/{actionType}")
    public String saveTransaction(@PathVariable String actionType, @ModelAttribute InventoryTransactionForm form, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        List<StockTx> transactions = form.getStockTxs().stream()
                .filter(tx -> tx.getInventoryId() != null && !tx.getInventoryId().isEmpty() && tx.getQtyChange() != null)
                .peek(tx -> {
                    tx.setCompanyId(userDetails.getCompanyId());
                    tx.setSiteId(form.getSiteId());
                    tx.setActionType(actionType);
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

    // ========== Ledger & Closing ==========

    @GetMapping("/ledger")
    public String stockLedger(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String siteId,
            @RequestParam(required = false) String storageId,
            @RequestParam(required = false) String inventoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        // Set default date range if not provided (e.g., this month)
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        List<InventoryLedgerDto> ledgerData = inventoryService.getInventoryLedger(
                userDetails.getCompanyId(), siteId, storageId, inventoryId, startDate, endDate);

        model.addAttribute("ledgerData", ledgerData);
        model.addAttribute("sites", siteService.getSitesByCompanyId(userDetails.getCompanyId()));
        // TODO: Add a way to get storages by site
        model.addAttribute("storages", storageService.getStoragesByCompanyId(userDetails.getCompanyId()));

        // Pass search params back to the view
        model.addAttribute("siteId", siteId);
        model.addAttribute("storageId", storageId);
        model.addAttribute("inventoryId", inventoryId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "inventory/inventoryStockLedger";
    }

    @GetMapping("/closing")
    public String closingForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Populate the form with necessary data
        model.addAttribute("sites", siteService.getSitesByCompanyId(userDetails.getCompanyId()));
        model.addAttribute("storages", storageService.getStoragesByCompanyId(userDetails.getCompanyId()));
        // Populate closing history
        model.addAttribute("closingHistory", inventoryService.getClosingHistory(userDetails.getCompanyId()));
        return "inventory/inventoryStockByMonth";
    }

    @PostMapping("/closing")
    public String closeMonth(
            @RequestParam String yyyymm,
            @RequestParam String siteId,
            @RequestParam(required = false) String storageId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        try {
            inventoryService.closeStockForMonth(userDetails.getCompanyId(), siteId, storageId, yyyymm);
            redirectAttributes.addFlashAttribute("successMessage", yyyymm + " 재고 마감이 성공적으로 실행되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "재고 마감 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/inventory/closing";
    }


    // ========== API ==========

    @GetMapping("/api/details")
    @ResponseBody
    public Inventory getInventoryDetails(@RequestParam String inventoryId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        InventoryId id = new InventoryId(userDetails.getCompanyId(), inventoryId);
        try {
            return inventoryService.getInventoryById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/download/excel")
    public void downloadExcel(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) throws IOException {
        String companyId = userDetails.getCompanyId();
        List<Inventory> inventories = inventoryService.getAllInventoriesByCompanyId(companyId);

        String[] headers = {"Item ID", "Item Name", "Type", "Dept ID", "Manufacturer", "Specification"};
        String[] fieldNames = {"inventoryId", "inventoryName", "masterType", "deptId", "makerName", "spec"};

        Workbook workbook = excelService.createWorkbook(inventories, headers, fieldNames);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=inventories.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

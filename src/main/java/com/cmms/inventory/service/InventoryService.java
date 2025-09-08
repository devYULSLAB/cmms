package com.cmms.inventory.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.inventory.dto.InventoryLedgerDto;
import com.cmms.inventory.entity.Inventory;
import com.cmms.inventory.entity.InventoryId;
import com.cmms.inventory.entity.InventoryStockByMonth;
import com.cmms.inventory.repository.InventoryRepository;
import com.cmms.inventory.repository.InventoryStockByMonthRepository;
import com.cmms.inventory.repository.StockTxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StockTxRepository stockTxRepository;
    private final IdGeneratorService idGeneratorService;
    private final InventoryStockByMonthRepository inventoryStockByMonthRepository;

    @Transactional(readOnly = true)
    public Page<Inventory> getInventoriesByCompanyId(String companyId, Pageable pageable) {
        return inventoryRepository.findInventoriesByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryById(InventoryId inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found: " + inventoryId));
    }

    @Transactional
    public Inventory saveInventory(Inventory inventory) {
        if (inventory.getInventoryId() == null || inventory.getInventoryId().isEmpty()) {
            inventory.setInventoryId(idGeneratorService.generateId(inventory.getCompanyId(), "2"));
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(InventoryId inventoryId) {
        if (hasRelatedData(inventoryId.getCompanyId(), inventoryId.getInventoryId())) {
            throw new RuntimeException("Cannot delete inventory with existing stock or transaction history.");
        }
        inventoryRepository.deleteById(inventoryId);
    }

    private boolean hasRelatedData(String companyId, String inventoryId) {
        // This logic needs to be more efficient.
        // For now, it's a placeholder for the check.
        return !stockTxRepository.findByCompanyIdAndInventoryIdOrderByCreatedAtDesc(companyId, inventoryId).isEmpty();
    }

    @Transactional(readOnly = true)
    public List<InventoryLedgerDto> getInventoryLedger(String companyId, String siteId, String storageId, String inventoryId, LocalDate startDate, LocalDate endDate) {
        // The native query returns a list of Object arrays. We need to map them to our DTO.
        List<Object[]> results = stockTxRepository.getInventoryLedgerData(
                companyId, siteId, storageId, inventoryId, startDate, endDate.plusDays(1));

        return results.stream().map(row -> new InventoryLedgerDto(
                (String) row[0],      // inventoryId
                (String) row[1],      // itemName
                (String) row[2],      // storageId
                (BigDecimal) row[3],  // beginningQty
                (BigDecimal) row[4],  // receivedQty
                (BigDecimal) row[5],  // issuedQty
                (BigDecimal) row[6],  // adjustedQty
                (BigDecimal) row[7]   // endingQty
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryStockByMonth> getClosingHistory(String companyId) {
        // This should be paginated in a real application
        return inventoryStockByMonthRepository.findByCompanyIdOrderByYyyymmDesc(companyId);
    }

    @Transactional
    public void closeStockForMonth(String companyId, String siteId, String storageId, String yyyymm) {
        // 1. --- Validation ---
        if (!yyyymm.matches("\\d{6}")) {
            throw new IllegalArgumentException("YYYYMM format is invalid. Must be 6 digits.");
        }
        LocalDate targetMonthStart = LocalDate.parse(yyyymm + "01", java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        java.time.LocalDateTime targetMonthStartDt = targetMonthStart.atStartOfDay();
        java.time.LocalDateTime targetMonthEndDt = targetMonthStart.plusMonths(1).atStartOfDay();

        List<InventoryStockByMonth> existingClosings = inventoryStockByMonthRepository.findByScope(companyId, yyyymm, siteId, storageId);
        if (!existingClosings.isEmpty()) {
            throw new IllegalStateException("The stock for " + yyyymm + " in the selected scope has already been closed.");
        }

        // 2. --- Data Fetching ---
        String previousYyyymm = targetMonthStart.minusMonths(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));
        List<InventoryStockByMonth> beginningBalances = inventoryStockByMonthRepository.findByScope(companyId, previousYyyymm, siteId, storageId);
        java.util.Map<String, BigDecimal> beginningBalanceMap = beginningBalances.stream()
                .collect(java.util.stream.Collectors.toMap(
                        b -> b.getInventoryId() + ":" + b.getStorageId(),
                        InventoryStockByMonth::getClosingQty
                ));

        List<Object[]> movements = stockTxRepository.getMonthlyMovements(companyId, siteId, storageId, targetMonthStartDt, targetMonthEndDt);
        java.util.Map<String, BigDecimal> movementMap = movements.stream()
                .collect(java.util.stream.Collectors.toMap(
                        m -> (String) m[0] + ":" + (String) m[1],
                        m -> (BigDecimal) m[2]
                ));

        java.util.Set<String> allKeys = new java.util.HashSet<>();
        beginningBalanceMap.keySet().forEach(allKeys::add);
        movementMap.keySet().forEach(allKeys::add);

        // 3. --- Calculation & Persistence ---
        List<InventoryStockByMonth> newClosings = new java.util.ArrayList<>();
        for (String key : allKeys) {
            String[] parts = key.split(":");
            String invId = parts[0];
            String storId = parts[1];

            BigDecimal beginningQty = beginningBalanceMap.getOrDefault(key, BigDecimal.ZERO);
            BigDecimal movementQty = movementMap.getOrDefault(key, BigDecimal.ZERO);
            BigDecimal endingQty = beginningQty.add(movementQty);

            InventoryStockByMonth newClosing = new InventoryStockByMonth();
            newClosing.setCompanyId(companyId);
            newClosing.setSiteId(siteId);
            newClosing.setStorageId(storId);
            newClosing.setInventoryId(invId);
            newClosing.setYyyymm(yyyymm);
            newClosing.setClosingQty(endingQty);
            newClosings.add(newClosing);
        }

        if (!newClosings.isEmpty()) {
            inventoryStockByMonthRepository.saveAll(newClosings);
        }
    }
}
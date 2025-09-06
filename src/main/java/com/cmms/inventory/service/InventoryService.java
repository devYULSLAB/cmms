package com.cmms.inventory.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.inventory.entity.Inventory;
import com.cmms.inventory.entity.InventoryId;
import com.cmms.inventory.repository.InventoryRepository;
import com.cmms.inventory.repository.StockRepository;
import com.cmms.inventory.repository.StockTxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StockRepository stockRepository;
    private final StockTxRepository stockTxRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional(readOnly = true)
    public Page<Inventory> findInventories(String companyId, Pageable pageable) {
        return inventoryRepository.findByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public Inventory findInventoryById(InventoryId inventoryId) {
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
}
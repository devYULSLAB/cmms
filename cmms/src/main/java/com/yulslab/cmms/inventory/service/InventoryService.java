package com.yulslab.cmms.inventory.service;

import com.yulslab.cmms.common.IdGenerationService;
import com.yulslab.cmms.inventory.entity.Inventory;
import com.yulslab.cmms.inventory.entity.InventoryId;
import com.yulslab.cmms.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final IdGenerationService idGenerationService;

    public List<Inventory> findInventories() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> findById(InventoryId inventoryId) {
        return inventoryRepository.findById(inventoryId);
    }

    @Transactional
    public Inventory saveInventory(Inventory inventory) {
        if (inventory.getId() == null || inventory.getId().getInventoryId() == null || inventory.getId().getInventoryId().isEmpty()) {
            String nextId = idGenerationService.generateNextId('2'); // '2' for inventory
            if (inventory.getId() == null) {
                inventory.setId(new InventoryId());
            }
            inventory.getId().setInventoryId(nextId);
            // Assuming companyId is set from the form/context
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(InventoryId inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }
}

package com.yulslab.cmms.inventory.service;

import com.yulslab.cmms.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    // Business logic for inventory management
}

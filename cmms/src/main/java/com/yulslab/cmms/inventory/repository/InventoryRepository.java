package com.yulslab.cmms.inventory.repository;

import com.yulslab.cmms.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}

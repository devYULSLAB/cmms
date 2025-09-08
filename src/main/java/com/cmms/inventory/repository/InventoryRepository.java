package com.cmms.inventory.repository;

import com.cmms.inventory.entity.Inventory;
import com.cmms.inventory.entity.InventoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

    Page<Inventory> findInventoriesByCompanyId(String companyId, Pageable pageable);

    List<Inventory> findInventoriesByCompanyId(String companyId);
    List<Inventory> findInventoriesByCompanyIdAndInventoryName(String companyId, String inventoryName);
    List<Inventory> findInventoriesByCompanyIdAndDeptId(String companyId, String deptId);
    List<Inventory> findInventoriesByCompanyIdAndMakerName(String companyId, String makerName);
    List<Inventory> findInventoriesByCompanyIdAndInventoryNameAndInventoryId(String companyId, String inventoryName, String inventoryId);
    Optional<Inventory> findInventoryByCompanyIdAndInventoryId(String companyId, String inventoryId);
}

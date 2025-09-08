package com.cmms.inventory.repository;

import com.cmms.inventory.entity.Inventory;
import com.cmms.inventory.entity.InventoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

    Page<Inventory> findInventoriesByCompanyId(String companyId, Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.companyId = :companyId AND i.siteId = :siteId AND (:storageId IS NULL OR i.storageId = :storageId)")
    List<Inventory> findInventoriesByScope(@Param("companyId") String companyId, @Param("siteId") String siteId, @Param("storageId") String storageId);
}

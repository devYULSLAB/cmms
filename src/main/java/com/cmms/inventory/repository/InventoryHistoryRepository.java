package com.cmms.inventory.repository;

import com.cmms.inventory.entity.InventoryHistory;
import com.cmms.inventory.entity.InventoryHistoryId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, InventoryHistoryId> {

    @Query("SELECT MAX(h.historyId) FROM InventoryHistory h WHERE h.companyId = :companyId")
    String findMaxHistoryIdByCompanyId(@Param("companyId") String companyId);

    List<InventoryHistory> findByCompanyIdAndInventoryIdOrderByTransactionDateDesc(String companyId,
            String inventoryId);

    List<InventoryHistory> findByCompanyIdOrderByTransactionDateDesc(String companyId);

}

package com.cmms.inventory.repository;

import com.cmms.inventory.entity.InventoryStockByMonth;
import com.cmms.inventory.entity.InventoryStockByMonthId;
import com.cmms.inventory.entity.InventoryStockByMonth;
import com.cmms.inventory.entity.InventoryStockByMonthId;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cmms.inventory.entity.InventoryStockByMonth;
import com.cmms.inventory.entity.InventoryStockByMonthId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryStockByMonthRepository extends JpaRepository<InventoryStockByMonth, InventoryStockByMonthId> {
    List<InventoryStockByMonth> findByCompanyIdOrderByYyyymmDesc(String companyId);

    Optional<InventoryStockByMonth> findTopByCompanyIdOrderByYyyymmDesc(String companyId);

    List<InventoryStockByMonth> findByCompanyIdAndYyyymmAndSiteId(String companyId, String yyyymm, String siteId);

    @Query("SELECT s FROM InventoryStockByMonth s WHERE s.companyId = :companyId AND s.yyyymm = :yyyymm AND s.siteId = :siteId AND (:storageId IS NULL OR s.storageId = :storageId)")
    List<InventoryStockByMonth> findByScope(@Param("companyId") String companyId, @Param("yyyymm") String yyyymm, @Param("siteId") String siteId, @Param("storageId") String storageId);
}

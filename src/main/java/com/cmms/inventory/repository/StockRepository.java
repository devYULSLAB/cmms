package com.cmms.inventory.repository;

import com.cmms.inventory.entity.Stock;
import com.cmms.inventory.entity.StockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, StockId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.companyId = :companyId AND s.siteId = :siteId AND s.storageId = :storageId AND s.inventoryId = :inventoryId")
    Optional<Stock> findByIdWithLock(@Param("companyId") String companyId,
                                    @Param("siteId") String siteId,
                                    @Param("storageId") String storageId,
                                    @Param("inventoryId") String inventoryId);

    Optional<Stock> findByCompanyIdAndSiteIdAndStorageIdAndInventoryId(String companyId, String siteId, String storageId, String inventoryId);
}

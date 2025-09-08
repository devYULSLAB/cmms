package com.cmms.inventory.repository;

import com.cmms.inventory.entity.StockByMonth;
import com.cmms.inventory.entity.StockByMonthId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockByMonthRepository extends JpaRepository<StockByMonth, StockByMonthId> {
    
    List<StockByMonth> findByCompanyIdAndSiteIdAndStorageIdAndYyyymm(String companyId, String siteId, String storageId, String yyyymm);
}

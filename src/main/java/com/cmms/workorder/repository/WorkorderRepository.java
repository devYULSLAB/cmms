package com.cmms.workorder.repository;

import com.cmms.workorder.entity.Workorder;
import com.cmms.workorder.entity.WorkorderId;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkorderRepository extends JpaRepository<Workorder, WorkorderId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(w.workorderId) FROM Workorder w WHERE w.companyId = :companyId")
    String findMaxWorkorderIdByCompanyId(@Param("companyId") String companyId);

    Page<Workorder> findByCompanyId(String companyId, Pageable pageable);
    Page<Workorder> findByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable);
    Page<Workorder> findByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);
    Page<Workorder> findByCompanyIdAndWorkorderNameContaining(String companyId, String workorderName, Pageable pageable);
}

package com.cmms.workpermit.repository;

import com.cmms.workpermit.entity.Workpermit;
import com.cmms.workpermit.entity.WorkpermitId;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkpermitRepository extends JpaRepository<Workpermit, WorkpermitId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(w.permitId) FROM Workpermit w WHERE w.companyId = :companyId")
    String findMaxPermitIdByCompanyId(@Param("companyId") String companyId);

    Page<Workpermit> findByCompanyId(String companyId, Pageable pageable);
    Page<Workpermit> findByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable);
    Page<Workpermit> findByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);
    Page<Workpermit> findByCompanyIdAndPermitNameContaining(String companyId, String permitName, Pageable pageable);
}

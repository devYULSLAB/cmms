package com.cmms.inspection.repository;

import com.cmms.inspection.entity.Inspection;
import com.cmms.inspection.entity.InspectionId;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, InspectionId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(i.inspectionId) FROM Inspection i WHERE i.companyId = :companyId")
    String findMaxInspectionIdByCompanyId(@Param("companyId") String companyId);

    Page<Inspection> findByCompanyId(String companyId, Pageable pageable);
    Page<Inspection> findByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable);
    Page<Inspection> findByCompanyIdAndDeptId(String companyId, String deptId, Pageable pageable);
    Page<Inspection> findByCompanyIdAndInspectionNameContaining(String companyId, String inspectionName, Pageable pageable);
    Page<Inspection> findByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);
}

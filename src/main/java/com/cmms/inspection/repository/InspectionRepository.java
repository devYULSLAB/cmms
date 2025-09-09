package com.cmms.inspection.repository;

import com.cmms.inspection.entity.Inspection;
import com.cmms.inspection.entity.InspectionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, InspectionId> {

    Page<Inspection> findInspectionsByCompanyId(String companyId, Pageable pageable);
    Page<Inspection> findInspectionsByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable);
    Page<Inspection> findInspectionsByCompanyIdAndDeptId(String companyId, String deptId, Pageable pageable);
    Page<Inspection> findInspectionsByCompanyIdAndInspectionNameContaining(String companyId, String inspectionName, Pageable pageable);
    Page<Inspection> findInspectionsByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);

    Optional<Inspection> findInspectionByCompanyIdAndInspectionId(String companyId, String inspectionId);

    List<Inspection> findTop5ByCompanyIdAndPlantIdOrderByPlannedDateDesc(String companyId, String plantId);
}

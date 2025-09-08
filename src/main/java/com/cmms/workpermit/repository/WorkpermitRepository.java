package com.cmms.workpermit.repository;

import com.cmms.workpermit.entity.Workpermit;
import com.cmms.workpermit.entity.WorkpermitId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkpermitRepository extends JpaRepository<Workpermit, WorkpermitId> {

    Page<Workpermit> findWorkpermitsByCompanyId(String companyId, Pageable pageable);
    Page<Workpermit> findWorkpermitsByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable);
    Page<Workpermit> findWorkpermitsByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);
    Page<Workpermit> findWorkpermitsByCompanyIdAndPermitNameContaining(String companyId, String permitName, Pageable pageable);
}

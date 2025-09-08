package com.cmms.workorder.repository;

import com.cmms.workorder.entity.Workorder;
import com.cmms.workorder.entity.WorkorderId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WorkorderRepository extends JpaRepository<Workorder, WorkorderId> {

    Page<Workorder> findWorkordersByCompanyId(String companyId, Pageable pageable);
    Page<Workorder> findWorkordersByCompanyIdAndSiteId(String companyId, String siteId, Pageable pageable);
    Page<Workorder> findWorkordersByCompanyIdAndPlantId(String companyId, String plantId, Pageable pageable);
    Page<Workorder> findWorkordersByCompanyIdAndWorkorderNameContaining(String companyId, String workorderName, Pageable pageable);

    Optional<Workorder> findWorkorderByCompanyIdAndWorkorderId(String companyId, String workorderId);
}

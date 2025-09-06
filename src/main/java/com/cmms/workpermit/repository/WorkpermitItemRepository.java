package com.cmms.workpermit.repository;

import com.cmms.workpermit.entity.WorkpermitItem;
import com.cmms.workpermit.entity.WorkpermitItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkpermitItemRepository extends JpaRepository<WorkpermitItem, WorkpermitItemId> {

    @Query("SELECT MAX(w.itemId) FROM WorkpermitItem w WHERE w.companyId = :companyId AND w.permitId = :permitId")
    String findMaxItemIdByCompanyIdAndPermitId(@Param("companyId") String companyId, @Param("permitId") String permitId);

    List<WorkpermitItem> findByCompanyIdAndPermitId(String companyId, String permitId);
}
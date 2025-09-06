package com.cmms.inspection.repository;

import com.cmms.inspection.entity.InspectionItem;
import com.cmms.inspection.entity.InspectionItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionItemRepository extends JpaRepository<InspectionItem, InspectionItemId> {

    List<InspectionItem> findByCompanyIdAndInspectionIdOrderByItemIdAsc(String companyId, String inspectionId);

    void deleteByCompanyIdAndInspectionId(String companyId, String inspectionId);

    List<InspectionItem> findByCompanyIdAndInspectionId(String companyId, String inspectionId);
}

package com.yulslab.cmms.inspection.repository;

import com.yulslab.cmms.inspection.entity.InspectionItem;
import com.yulslab.cmms.inspection.entity.InspectionItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionItemRepository extends JpaRepository<InspectionItem, InspectionItemId> {
}

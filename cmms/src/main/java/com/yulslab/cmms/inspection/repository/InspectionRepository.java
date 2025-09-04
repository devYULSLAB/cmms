package com.yulslab.cmms.inspection.repository;

import com.yulslab.cmms.inspection.entity.Inspection;
import com.yulslab.cmms.inspection.entity.InspectionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionRepository extends JpaRepository<Inspection, InspectionId> {
}

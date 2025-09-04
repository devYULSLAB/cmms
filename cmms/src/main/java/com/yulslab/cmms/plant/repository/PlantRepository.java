package com.yulslab.cmms.plant.repository;

import com.yulslab.cmms.plant.entity.Plant;
import com.yulslab.cmms.plant.entity.PlantId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, PlantId> {
    List<Plant> findById_CompanyIdAndId_SiteId(String companyId, String siteId);
}

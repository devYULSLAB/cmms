package com.yulslab.cmms.plant.repository;

import com.yulslab.cmms.plant.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}

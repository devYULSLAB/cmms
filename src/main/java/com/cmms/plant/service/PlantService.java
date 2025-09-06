package com.cmms.plant.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.plant.entity.Plant;
import com.cmms.plant.entity.PlantId;
import com.cmms.plant.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional(readOnly = true)
    public Page<Plant> findPlants(String companyId, Pageable pageable) {
        return plantRepository.findByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public Plant findPlantById(PlantId plantId) {
        return plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found: " + plantId));
    }

    @Transactional
    public Plant savePlant(Plant plant) {
        if (plant.getPlantId() == null || plant.getPlantId().isEmpty()) {
            plant.setPlantId(idGeneratorService.generateId(plant.getCompanyId(), "1"));
        }
        return plantRepository.save(plant);
    }

    @Transactional
    public void deletePlant(PlantId plantId) {
        plantRepository.deleteById(plantId);
    }
}
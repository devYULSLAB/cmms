package com.yulslab.cmms.plant.service;

import com.yulslab.cmms.plant.entity.Plant;
import com.yulslab.cmms.common.IdGenerationService;
import com.yulslab.cmms.plant.entity.PlantId;
import com.yulslab.cmms.plant.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantService {

    private final PlantRepository plantRepository;
    private final IdGenerationService idGenerationService;

    public List<Plant> findPlants() {
        return plantRepository.findAll();
    }

    public Optional<Plant> findById(PlantId plantId) {
        return plantRepository.findById(plantId);
    }

    public List<Plant> findPlantsBySite(String companyId, String siteId) {
        return plantRepository.findById_CompanyIdAndId_SiteId(companyId, siteId);
    }

    @Transactional
    public Plant savePlant(Plant plant) {
        if (plant.getId() == null || plant.getId().getPlantId() == null || plant.getId().getPlantId().isEmpty()) {
            String nextId = idGenerationService.generateNextId('1'); // '1' for plant
            if (plant.getId() == null) {
                plant.setId(new PlantId());
            }
            plant.getId().setPlantId(nextId);
            // Assuming companyId and siteId are set from the form
        }
        return plantRepository.save(plant);
    }

    @Transactional
    public void deletePlant(PlantId plantId) {
        plantRepository.deleteById(plantId);
    }
}

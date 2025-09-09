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

import java.util.List;

/**
 * 프로그램명: CMMS 설비 관리 서비스
 * 기능: 설비 등록, 조회, 수정, 삭제 및 ID 생성 관리
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional(readOnly = true)
    public Page<Plant> getPlantsByCompanyId(String companyId, Pageable pageable) {
        return plantRepository.findPlantsByCompanyId(companyId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Plant> getAllPlantsByCompanyId(String companyId) {
        return plantRepository.findPlantsByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Plant getPlantById(PlantId plantId) {
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
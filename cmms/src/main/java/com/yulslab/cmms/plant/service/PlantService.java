package com.yulslab.cmms.plant.service;

import com.yulslab.cmms.plant.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    // Basic CRUD methods will be added here
}

package com.yulslab.cmms.plant.controller;

import com.yulslab.cmms.plant.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/plant")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    @GetMapping("/plantList")
    public String plantList() {
        return "plant/plantList";
    }

    @GetMapping("/plantForm")
    public String plantForm() {
        return "plant/plantForm";
    }

    @GetMapping("/plantDetail")
    public String plantDetail() {
        return "plant/plantDetail";
    }
}

package com.yulslab.cmms.plant.controller;

import com.yulslab.cmms.plant.entity.Plant;
import com.yulslab.cmms.plant.entity.PlantId;
import com.yulslab.cmms.plant.service.PlantService;
import com.yulslab.domain.common.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/plant")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;
    private final DomainService domainService;

    @Value("${app.default-company-code}")
    private String defaultCompanyId;

    @GetMapping("/plantList")
    public String plantList(Model model) {
        List<Plant> plants = plantService.findPlants();
        model.addAttribute("plants", plants);
        return "plant/plantList";
    }

    @GetMapping("/plantDetail")
    public String plantDetail(@RequestParam String companyId, @RequestParam String siteId, @RequestParam String plantId, Model model) {
        PlantId pId = new PlantId();
        pId.setCompanyId(companyId);
        pId.setSiteId(siteId);
        pId.setPlantId(plantId);
        plantService.findById(pId).ifPresent(plant -> model.addAttribute("plant", plant));
        return "plant/plantDetail";
    }

    @GetMapping("/plantForm")
    public String plantForm(@RequestParam(required = false) String companyId,
                            @RequestParam(required = false) String siteId,
                            @RequestParam(required = false) String plantId,
                            Model model) {
        if (plantId != null) {
            PlantId pId = new PlantId();
            pId.setCompanyId(companyId);
            pId.setSiteId(siteId);
            pId.setPlantId(plantId);
            plantService.findById(pId).ifPresent(plant -> model.addAttribute("plant", plant));
        } else {
            model.addAttribute("plant", new Plant());
        }

        // Add data for dropdowns
        model.addAttribute("sites", domainService.getSites(defaultCompanyId));
        model.addAttribute("depts", domainService.getDepts(defaultCompanyId));
        model.addAttribute("funcs", domainService.getFuncs(defaultCompanyId));
        model.addAttribute("assetTypes", domainService.getCodes(defaultCompanyId, "ASSET"));
        model.addAttribute("depreTypes", domainService.getCodes(defaultCompanyId, "DEPRE"));

        return "plant/plantForm";
    }

    @PostMapping("/save")
    public String savePlant(@ModelAttribute("plant") Plant plant) {
        // For new entities, the companyId is not bound from the form, set it here.
        if (plant.getId() != null && plant.getId().getCompanyId() == null) {
            plant.getId().setCompanyId(defaultCompanyId);
        }
        plantService.savePlant(plant);
        return "redirect:/plant/plantList";
    }
}

package com.yulslab.cmms.inspection.controller;

import com.yulslab.cmms.inspection.entity.Inspection;
import com.yulslab.cmms.inspection.entity.InspectionId;
import com.yulslab.cmms.inspection.service.InspectionService;
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
@RequestMapping("/inspection")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService inspectionService;
    private final DomainService domainService;
    private final PlantService plantService;

    @Value("${app.default-company-code}")
    private String defaultCompanyId;

    @GetMapping("/inspectionList")
    public String inspectionList(Model model) {
        List<Inspection> inspections = inspectionService.findInspections();
        model.addAttribute("inspections", inspections);
        return "inspection/inspectionList";
    }

    @GetMapping("/inspectionForm")
    public String inspectionForm(@RequestParam(required = false) String inspectionId,
                                 @RequestParam(required = false) String siteId,
                                 Model model) {
        if (inspectionId != null) {
            InspectionId iId = new InspectionId();
            iId.setCompanyId(defaultCompanyId);
            iId.setSiteId(siteId);
            iId.setInspectionId(inspectionId);
            inspectionService.findById(iId).ifPresent(inspection -> model.addAttribute("inspection", inspection));
        } else {
            model.addAttribute("inspection", new Inspection());
        }

        model.addAttribute("sites", domainService.getSites(defaultCompanyId));
        model.addAttribute("depts", domainService.getDepts(defaultCompanyId));
        model.addAttribute("plants", plantService.findPlants()); // Simplified for now
        model.addAttribute("jobTypes", domainService.getCodes(defaultCompanyId, "JOBTP"));
        return "inspection/inspectionForm";
    }

    @PostMapping("/save")
    public String saveInspection(@ModelAttribute("inspection") Inspection inspection) {
        if (inspection.getId() != null && inspection.getId().getCompanyId() == null) {
            inspection.getId().setCompanyId(defaultCompanyId);
        }
        inspectionService.saveInspection(inspection);
        return "redirect:/inspection/inspectionList";
    }
}

package com.cmms.inspection.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.inspection.entity.Inspection;
import com.cmms.inspection.entity.InspectionId;
import com.cmms.inspection.entity.InspectionItem;
import com.cmms.inspection.service.InspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inspection")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService inspectionService;
    // Assuming other services will be refactored and injected
    // private final SiteService siteService;
    // private final DeptService deptService;
    // private final CommonCodeService commonCodeService;

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PageableDefault(size = 10, sort = "inspectionId") Pageable pageable) {
        Page<Inspection> inspections = inspectionService.getInspectionsByCompanyId(userDetails.getCompanyId(), pageable);
        model.addAttribute("inspections", inspections);
        return "inspection/list";
    }

    @GetMapping("/form")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Inspection inspection = new Inspection();
        List<InspectionItem> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(new InspectionItem());
        }
        inspection.setItems(items);
        model.addAttribute("inspection", inspection);
        // model.addAttribute("sites", siteService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("jobTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "JOBTP"));
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        return "inspection/form";
    }

    @GetMapping("/{inspectionId}/edit")
    public String editForm(@PathVariable String inspectionId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        InspectionId id = new InspectionId(userDetails.getCompanyId(), inspectionId);
        Inspection inspection = inspectionService.getInspectionById(id);
        model.addAttribute("inspection", inspection);
        // model.addAttribute("sites", siteService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("jobTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "JOBTP"));
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        return "inspection/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Inspection inspection, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        inspection.setCompanyId(userDetails.getCompanyId());
        inspectionService.saveInspection(inspection);
        redirectAttributes.addFlashAttribute("message", "Inspection saved successfully.");
        return "redirect:/inspection/list";
    }

    @GetMapping("/{inspectionId}")
    public String detail(@PathVariable String inspectionId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        InspectionId id = new InspectionId(userDetails.getCompanyId(), inspectionId);
        model.addAttribute("inspection", inspectionService.getInspectionById(id));
        return "inspection/detail";
    }

    @PostMapping("/{inspectionId}/delete")
    public String delete(@PathVariable String inspectionId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        InspectionId id = new InspectionId(userDetails.getCompanyId(), inspectionId);
        try {
            inspectionService.deleteInspection(id);
            redirectAttributes.addFlashAttribute("message", "Inspection deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting inspection: " + e.getMessage());
        }
        return "redirect:/inspection/list";
    }
}

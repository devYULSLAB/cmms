package com.cmms.workpermit.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.workpermit.entity.Workpermit;
import com.cmms.workpermit.entity.WorkpermitId;
import com.cmms.workpermit.entity.WorkpermitItem;
import com.cmms.workpermit.service.WorkpermitService;
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
@RequestMapping("/workpermit")
@RequiredArgsConstructor
public class WorkpermitController {

    private final WorkpermitService workpermitService;
    // Assuming other services will be refactored and injected
    // private final SiteService siteService;
    // private final DeptService deptService;
    // private final CommonCodeService commonCodeService;

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PageableDefault(size = 10, sort = "permitId") Pageable pageable) {
        Page<Workpermit> workpermits = workpermitService.getWorkpermitsByCompanyId(userDetails.getCompanyId(), pageable);
        model.addAttribute("workpermits", workpermits);
        return "workpermit/list";
    }

    @GetMapping("/form")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Workpermit workpermit = new Workpermit();
        List<WorkpermitItem> items = new ArrayList<>();
        items.add(new WorkpermitItem());
        workpermit.setItems(items);
        model.addAttribute("workpermit", workpermit);
        // model.addAttribute("sites", siteService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("permitTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "PERMT"));
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        return "workpermit/form";
    }

    @GetMapping("/{permitId}/edit")
    public String editForm(@PathVariable String permitId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkpermitId id = new WorkpermitId(userDetails.getCompanyId(), permitId);
        Workpermit workpermit = workpermitService.getWorkpermitById(id);
        model.addAttribute("workpermit", workpermit);
        // model.addAttribute("sites", siteService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("permitTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "PERMT"));
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        return "workpermit/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Workpermit workpermit, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        workpermit.setCompanyId(userDetails.getCompanyId());
        workpermitService.saveWorkpermit(workpermit);
        redirectAttributes.addFlashAttribute("message", "Work permit saved successfully.");
        return "redirect:/workpermit/list";
    }

    @GetMapping("/{permitId}")
    public String detail(@PathVariable String permitId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkpermitId id = new WorkpermitId(userDetails.getCompanyId(), permitId);
        model.addAttribute("workpermit", workpermitService.getWorkpermitById(id));
        return "workpermit/detail";
    }

    @PostMapping("/{permitId}/delete")
    public String delete(@PathVariable String permitId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        WorkpermitId id = new WorkpermitId(userDetails.getCompanyId(), permitId);
        try {
            workpermitService.deleteWorkpermit(id);
            redirectAttributes.addFlashAttribute("message", "Work permit deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting work permit: " + e.getMessage());
        }
        return "redirect:/workpermit/list";
    }
}

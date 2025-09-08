package com.cmms.workorder.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.workorder.entity.Workorder;
import com.cmms.workorder.entity.WorkorderId;
import com.cmms.workorder.entity.WorkorderItem;
import com.cmms.workorder.service.WorkorderService;
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
@RequestMapping("/workorder")
@RequiredArgsConstructor
public class WorkorderController {

    private final WorkorderService workorderService;

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PageableDefault(size = 10, sort = "workorderId") Pageable pageable) {
        Page<Workorder> workorders = workorderService.getWorkordersByCompanyId(userDetails.getCompanyId(), pageable);
        model.addAttribute("workorders", workorders);
        return "workorder/list";
    }

    @GetMapping("/form")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Workorder workorder = new Workorder();
        List<WorkorderItem> items = new ArrayList<>();
        items.add(new WorkorderItem());
        workorder.setItems(items);
        model.addAttribute("workorder", workorder);
        // model.addAttribute("sites", siteService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("jobTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "JOBTP"));
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        return "workorder/form";
    }

    @GetMapping("/{workorderId}/edit")
    public String editForm(@PathVariable String workorderId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkorderId id = new WorkorderId(userDetails.getCompanyId(), workorderId);
        Workorder workorder = workorderService.getWorkorderById(id);
        model.addAttribute("workorder", workorder);
        // model.addAttribute("sites", siteService.findAllByCompanyId(userDetails.getCompanyId()));
        // model.addAttribute("jobTypes", commonCodeService.findByCodeType(userDetails.getCompanyId(), "JOBTP"));
        // model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        return "workorder/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Workorder workorder, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        workorder.setCompanyId(userDetails.getCompanyId());
        workorderService.saveWorkorder(workorder);
        redirectAttributes.addFlashAttribute("message", "Workorder saved successfully.");
        return "redirect:/workorder/list";
    }

    @GetMapping("/{workorderId}")
    public String detail(@PathVariable String workorderId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        WorkorderId id = new WorkorderId(userDetails.getCompanyId(), workorderId);
        model.addAttribute("workorder", workorderService.getWorkorderById(id));
        return "workorder/detail";
    }

    @PostMapping("/{workorderId}/delete")
    public String delete(@PathVariable String workorderId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        WorkorderId id = new WorkorderId(userDetails.getCompanyId(), workorderId);
        try {
            workorderService.deleteWorkorder(id);
            redirectAttributes.addFlashAttribute("message", "Workorder deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting workorder: " + e.getMessage());
        }
        return "redirect:/workorder/list";
    }
}
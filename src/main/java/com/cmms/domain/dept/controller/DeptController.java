package com.cmms.domain.dept.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.domain.dept.entity.Dept;
import com.cmms.domain.dept.entity.DeptId;
import com.cmms.domain.dept.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/domain/dept")
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    @GetMapping
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("depts", deptService.getDeptsByCompanyId(userDetails.getCompanyId()));
        return "domain/dept/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("dept", new Dept());
        return "domain/dept/form";
    }

    @PostMapping
    public String save(@ModelAttribute Dept dept, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        dept.setCompanyId(userDetails.getCompanyId());
        try {
            deptService.saveDept(dept);
            redirectAttributes.addFlashAttribute("message", "Department saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving department: " + e.getMessage());
            return "redirect:/domain/dept/new";
        }
        return "redirect:/domain/dept";
    }

    @GetMapping("/{companyId}/{deptId}")
    public String detail(@PathVariable String companyId, @PathVariable String deptId, Model model) {
        DeptId id = new DeptId(companyId, deptId);
        model.addAttribute("dept", deptService.getDeptById(id));
        return "domain/dept/detail";
    }

    @GetMapping("/{companyId}/{deptId}/edit")
    public String editForm(@PathVariable String companyId, @PathVariable String deptId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        DeptId id = new DeptId(companyId, deptId);
        model.addAttribute("dept", deptService.getDeptById(id));
        return "domain/dept/form";
    }

    @PostMapping("/{companyId}/{deptId}")
    public String update(@PathVariable String companyId, @PathVariable String deptId, @ModelAttribute Dept dept, RedirectAttributes redirectAttributes) {
        dept.setCompanyId(companyId);
        dept.setDeptId(deptId);
        try {
            deptService.saveDept(dept);
            redirectAttributes.addFlashAttribute("message", "Department updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating department: " + e.getMessage());
            return "redirect:/domain/dept/" + companyId + "/" + deptId + "/edit";
        }
        return "redirect:/domain/dept";
    }

    @PostMapping("/{companyId}/{deptId}/delete")
    public String delete(@PathVariable String companyId, @PathVariable String deptId, RedirectAttributes redirectAttributes) {
        DeptId id = new DeptId(companyId, deptId);
        try {
            deptService.deleteDept(id);
            redirectAttributes.addFlashAttribute("message", "Department deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/domain/dept";
    }
}

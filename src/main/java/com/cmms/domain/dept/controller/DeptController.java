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

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("depts", deptService.findAllByCompanyId(userDetails.getCompanyId()));
        return "domain/dept/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("dept", new Dept());
        return "domain/dept/form";
    }

    @GetMapping("/{deptId}/edit")
    public String editForm(@PathVariable String deptId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        DeptId id = new DeptId(userDetails.getCompanyId(), deptId);
        model.addAttribute("dept", deptService.findDeptById(id));
        return "domain/dept/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Dept dept, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        dept.setCompanyId(userDetails.getCompanyId());
        try {
            deptService.saveDept(dept);
            redirectAttributes.addFlashAttribute("message", "Department saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving department: " + e.getMessage());
            return "redirect:/domain/dept/form";
        }
        return "redirect:/domain/dept/list";
    }

    @PostMapping("/{deptId}/delete")
    public String delete(@PathVariable String deptId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        DeptId id = new DeptId(userDetails.getCompanyId(), deptId);
        try {
            deptService.deleteDept(id);
            redirectAttributes.addFlashAttribute("message", "Department deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting department: " + e.getMessage());
        }
        return "redirect:/domain/dept/list";
    }
}

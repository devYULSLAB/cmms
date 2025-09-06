package com.cmms.domain.company.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.domain.company.entity.Company;
import com.cmms.domain.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/domain/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("companies", companyService.findAllCompanies());
        return "domain/company/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("company", new Company());
        return "domain/company/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("company", companyService.findCompanyById(id));
        return "domain/company/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Company company, RedirectAttributes redirectAttributes) {
        try {
            companyService.saveCompany(company);
            redirectAttributes.addFlashAttribute("message", "Company saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving company: " + e.getMessage());
            return "redirect:/domain/company/form";
        }
        return "redirect:/domain/company/list";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            companyService.deleteCompany(id);
            redirectAttributes.addFlashAttribute("message", "Company deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting company: " + e.getMessage());
        }
        return "redirect:/domain/company/list";
    }
}

package com.cmms.domain.role.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.domain.role.entity.Role;
import com.cmms.domain.role.entity.RoleId;
import com.cmms.domain.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/domain/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("roles", roleService.getRolesByCompanyId(userDetails.getCompanyId()));
        return "domain/role/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("role", new Role());
        return "domain/role/form";
    }

    @PostMapping
    public String save(@ModelAttribute Role role, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        role.setCompanyId(userDetails.getCompanyId());
        try {
            roleService.saveRole(role);
            redirectAttributes.addFlashAttribute("message", "Role saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving role: " + e.getMessage());
            return "redirect:/domain/role/new";
        }
        return "redirect:/domain/role";
    }

    @GetMapping("/{companyId}/{roleId}")
    public String detail(@PathVariable String companyId, @PathVariable String roleId, Model model) {
        RoleId id = new RoleId(companyId, roleId);
        model.addAttribute("role", roleService.getRoleById(id));
        return "domain/role/detail";
    }

    @GetMapping("/{companyId}/{roleId}/edit")
    public String editForm(@PathVariable String companyId, @PathVariable String roleId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        RoleId id = new RoleId(companyId, roleId);
        model.addAttribute("role", roleService.getRoleById(id));
        return "domain/role/form";
    }

    @PostMapping("/{companyId}/{roleId}")
    public String update(@PathVariable String companyId, @PathVariable String roleId, @ModelAttribute Role role, RedirectAttributes redirectAttributes) {
        role.setCompanyId(companyId);
        role.setRoleId(roleId);
        try {
            roleService.saveRole(role);
            redirectAttributes.addFlashAttribute("message", "Role updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating role: " + e.getMessage());
            return "redirect:/domain/role/" + companyId + "/" + roleId + "/edit";
        }
        return "redirect:/domain/role";
    }

    @PostMapping("/{companyId}/{roleId}/delete")
    public String delete(@PathVariable String companyId, @PathVariable String roleId, RedirectAttributes redirectAttributes) {
        RoleId id = new RoleId(companyId, roleId);
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("message", "Role deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting role: " + e.getMessage());
        }
        return "redirect:/domain/role";
    }
}

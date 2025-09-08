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

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("roles", roleService.getRolesByCompanyId(userDetails.getCompanyId()));
        return "domain/role/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("role", new Role());
        return "domain/role/form";
    }

    @GetMapping("/{roleId}/edit")
    public String editForm(@PathVariable String roleId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        RoleId id = new RoleId(userDetails.getCompanyId(), roleId);
        model.addAttribute("role", roleService.getRoleById(id));
        return "domain/role/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Role role, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        role.setCompanyId(userDetails.getCompanyId());
        try {
            roleService.saveRole(role);
            redirectAttributes.addFlashAttribute("message", "Role saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving role: " + e.getMessage());
            return "redirect:/domain/role/form";
        }
        return "redirect:/domain/role/list";
    }

    @PostMapping("/{roleId}/delete")
    public String delete(@PathVariable String roleId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        RoleId id = new RoleId(userDetails.getCompanyId(), roleId);
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("message", "Role deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting role: " + e.getMessage());
        }
        return "redirect:/domain/role/list";
    }
}

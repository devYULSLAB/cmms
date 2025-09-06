package com.cmms.domain.user.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.domain.role.service.RoleService;
import com.cmms.domain.user.entity.User;
import com.cmms.domain.user.entity.UserId;
import com.cmms.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/domain/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("users", userService.findAllByCompanyId(userDetails.getCompanyId()));
        return "domain/user/list";
    }

    @GetMapping("/form")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAllRolesByCompanyId(userDetails.getCompanyId()));
        return "domain/user/form";
    }

    @GetMapping("/{userId}/edit")
    public String editForm(@PathVariable String userId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserId id = new UserId(userDetails.getCompanyId(), userId);
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("roles", roleService.findAllRolesByCompanyId(userDetails.getCompanyId()));
        // Note: Need to also load the user's current roles
        return "domain/user/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        user.setCompanyId(userDetails.getCompanyId());
        // TODO: Handle password encoding and setting user roles
        try {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("message", "User saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving user: " + e.getMessage());
            return "redirect:/domain/user/form";
        }
        return "redirect:/domain/user/list";
    }

    @PostMapping("/{userId}/delete")
    public String delete(@PathVariable String userId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        UserId id = new UserId(userDetails.getCompanyId(), userId);
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/domain/user/list";
    }
}

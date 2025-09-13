package com.cmms.domain.user.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.domain.user.entity.User;
import com.cmms.domain.user.entity.UserId;
import com.cmms.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/domain/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("users", userService.getUsersByCompanyId(userDetails.getCompanyId()));
        return "domain/user/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "domain/user/form";
    }

    @PostMapping
    public String save(@ModelAttribute("user") User formUser,
                       @RequestParam(name = "rawPassword", required = false) String rawPassword,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       RedirectAttributes redirectAttributes) {
        try {
            // Set companyId from the authenticated user
            formUser.setCompanyId(userDetails.getCompanyId());
            userService.saveUser(formUser, rawPassword);
            redirectAttributes.addFlashAttribute("message", "User saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving user: " + e.getMessage());
            return "redirect:/domain/user/new";
        }
        return "redirect:/domain/user";
    }

    @GetMapping("/{companyId}/{userId}")
    public String detail(@PathVariable String companyId, @PathVariable String userId, Model model) {
        UserId id = new UserId(companyId, userId);
        model.addAttribute("user", userService.getUserById(id));
        return "domain/user/detail";
    }

    @GetMapping("/{companyId}/{userId}/edit")
    public String editForm(@PathVariable String companyId, @PathVariable String userId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserId id = new UserId(companyId, userId);
        model.addAttribute("user", userService.getUserById(id));
        return "domain/user/form";
    }

    @PostMapping("/{companyId}/{userId}")
    public String update(@PathVariable String companyId, @PathVariable String userId, 
                        @ModelAttribute("user") User formUser,
                        @RequestParam(name = "rawPassword", required = false) String rawPassword,
                        RedirectAttributes redirectAttributes) {
        try {
            formUser.setCompanyId(companyId);
            formUser.setUserId(userId);
            userService.saveUser(formUser, rawPassword);
            redirectAttributes.addFlashAttribute("message", "User updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
            return "redirect:/domain/user/" + companyId + "/" + userId + "/edit";
        }
        return "redirect:/domain/user";
    }

    @PostMapping("/{companyId}/{userId}/delete")
    public String delete(@PathVariable String companyId, @PathVariable String userId, RedirectAttributes redirectAttributes) {
        UserId id = new UserId(companyId, userId);
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/domain/user";
    }
}

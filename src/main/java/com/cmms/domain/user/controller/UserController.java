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

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("users", userService.findAllByCompanyId(userDetails.getCompanyId()));
        return "domain/user/userList";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "domain/user/userForm";
    }

    @GetMapping("/{userId}/edit")
    public String editForm(@PathVariable String userId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserId id = new UserId(userDetails.getCompanyId(), userId);
        model.addAttribute("user", userService.findUserById(id));
        return "domain/user/userForm";
    }

    @PostMapping("/save")
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
            // It's better to redirect to the form again to show the error
            return "redirect:/domain/user/form?userId=" + formUser.getUserId();
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

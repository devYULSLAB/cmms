package com.cmms.auth.controller;

import com.cmms.domain.user.entity.User;
import com.cmms.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/")
    public String home(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/memo/memoList";
        }
        return "redirect:/login";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@RequestParam String companyId,
                         @RequestParam String userId,
                         @RequestParam String password,
                         RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setCompanyId(companyId);
            user.setUserId(userId);
            user.setUserName(userId); // 기본적으로 userName을 userId와 동일하게 설정
            user.setPasswordHash(passwordEncoder.encode(password));
            user.setPasswordUpdatedAt(LocalDateTime.now());
            user.setFailedLoginCount(0);
            user.setIsLocked("Y"); // 회원가입 시 기본으로 잠금
            user.setMustChangePw("N");
            user.setCreateDate(LocalDateTime.now());
            user.setUpdateDate(LocalDateTime.now());

            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("successMessage", "회원가입 요청이 완료되었습니다. 관리자의 승인을 기다려주세요.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원가입 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/auth/signup";
        }
    }

    @GetMapping("/auth/find-id")
    public String findIdForm() {
        return "auth/find_id";
    }

    @PostMapping("/auth/find-id")
    public String findId(@RequestParam String companyId,
                         @RequestParam String userId,
                         RedirectAttributes redirectAttributes) {
        try {
            if (userService.isUserExist(companyId, userId)) {
                redirectAttributes.addFlashAttribute("message", "ID '" + userId + "'는 존재하는 아이디입니다.");
            } else {
                redirectAttributes.addFlashAttribute("message", "ID '" + userId + "'는 존재하지 않는 아이디입니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "ID 조회 중 오류가 발생했습니다.");
        }
        redirectAttributes.addAttribute("companyId", companyId);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/auth/find-id";
    }
}

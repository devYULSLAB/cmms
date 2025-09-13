package com.cmms.func.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.func.entity.Func;
import com.cmms.func.entity.FuncId;
import com.cmms.func.service.FuncService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/func")
@RequiredArgsConstructor
public class FuncController {

    private final FuncService funcService;

    @GetMapping
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("funcs", funcService.getFuncsByCompanyId(userDetails.getCompanyId()));
        return "func/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("func", new Func());
        return "func/form";
    }

    @PostMapping
    public String save(@ModelAttribute Func func, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        func.setCompanyId(userDetails.getCompanyId());
        try {
            funcService.saveFunc(func);
            redirectAttributes.addFlashAttribute("message", "Function location saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving function location: " + e.getMessage());
            return "redirect:/func/new";
        }
        return "redirect:/func";
    }

    @GetMapping("/{funcId}")
    public String detail(@PathVariable String funcId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        FuncId id = new FuncId(userDetails.getCompanyId(), funcId);
        model.addAttribute("func", funcService.getFuncById(id));
        return "func/detail";
    }

    @GetMapping("/{funcId}/edit")
    public String editForm(@PathVariable String funcId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        FuncId id = new FuncId(userDetails.getCompanyId(), funcId);
        model.addAttribute("func", funcService.getFuncById(id));
        return "func/form";
    }

    @PostMapping("/{funcId}")
    public String update(@PathVariable String funcId, @ModelAttribute Func func, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        func.setCompanyId(userDetails.getCompanyId());
        func.setFuncId(funcId);
        try {
            funcService.saveFunc(func);
            redirectAttributes.addFlashAttribute("message", "Function location updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating function location: " + e.getMessage());
            return "redirect:/func/" + funcId + "/edit";
        }
        return "redirect:/func";
    }

    @PostMapping("/{funcId}/delete")
    public String delete(@PathVariable String funcId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        FuncId id = new FuncId(userDetails.getCompanyId(), funcId);
        try {
            funcService.deleteFunc(id);
            redirectAttributes.addFlashAttribute("message", "Function location deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting function location: " + e.getMessage());
        }
        return "redirect:/func";
    }
}
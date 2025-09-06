package com.cmms.common.code.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.common.code.entity.Code;
import com.cmms.common.code.entity.CodeId;
import com.cmms.common.code.entity.CodeType;
import com.cmms.common.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/common/code")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String companyId = userDetails.getCompanyId();
        model.addAttribute("codeTypes", codeService.findCodeTypesByCompany(companyId));
        // You might want to display codes for the first type by default
        // model.addAttribute("codes", codeService.findCodesByCompanyAndType(companyId, "FIRST_TYPE"));
        return "common/code/list";
    }

    @GetMapping("/type/form")
    public String typeForm(Model model) {
        model.addAttribute("codeType", new CodeType());
        return "common/code/type-form";
    }

    @PostMapping("/type/save")
    public String saveType(@ModelAttribute CodeType codeType, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        codeType.setCompanyId(userDetails.getCompanyId());
        codeService.saveCodeType(codeType);
        redirectAttributes.addFlashAttribute("message", "Code Type saved successfully.");
        return "redirect:/common/code/list";
    }

    @GetMapping("/form")
    public String codeForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("code", new Code());
        model.addAttribute("codeTypes", codeService.findCodeTypesByCompany(userDetails.getCompanyId()));
        return "common/code/form";
    }

    @PostMapping("/save")
    public String saveCode(@ModelAttribute Code code, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        code.setCompanyId(userDetails.getCompanyId());
        codeService.saveCode(code);
        redirectAttributes.addFlashAttribute("message", "Code saved successfully.");
        return "redirect:/common/code/list";
    }
}
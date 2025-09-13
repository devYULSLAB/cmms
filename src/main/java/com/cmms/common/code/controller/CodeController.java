package com.cmms.common.code.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.common.code.entity.Code;
import com.cmms.common.code.entity.CodeId;
import com.cmms.common.code.entity.CodeType;
import com.cmms.common.code.entity.CodeTypeId;
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

    @GetMapping
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String companyId = userDetails.getCompanyId();
        model.addAttribute("codeTypes", codeService.getCodeTypesByCompany(companyId));
        return "common/code/list";
    }

    @GetMapping("/new")
    public String form(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("code", new Code());
        model.addAttribute("codeTypes", codeService.getCodeTypesByCompany(userDetails.getCompanyId()));
        return "common/code/form";
    }

    @PostMapping
    public String save(@ModelAttribute Code code, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        code.setCompanyId(userDetails.getCompanyId());
        codeService.saveCode(code);
        redirectAttributes.addFlashAttribute("message", "Code saved successfully.");
        return "redirect:/common/code";
    }

    @GetMapping("/{companyId}/{codeId}/edit")
    public String editForm(@PathVariable String companyId, @PathVariable String codeId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        CodeId id = new CodeId(companyId, codeId);
        Code code = codeService.getCodeById(id);
        model.addAttribute("code", code);
        model.addAttribute("codeTypes", codeService.getCodeTypesByCompany(userDetails.getCompanyId()));
        return "common/code/form";
    }

    @PostMapping("/{companyId}/{codeId}")
    public String update(@PathVariable String companyId, @PathVariable String codeId, @ModelAttribute Code code, RedirectAttributes redirectAttributes) {
        code.setCompanyId(companyId);
        code.setCodeId(codeId);
        codeService.saveCode(code);
        redirectAttributes.addFlashAttribute("message", "Code updated successfully.");
        return "redirect:/common/code";
    }

    @PostMapping("/{companyId}/{codeId}/delete")
    public String delete(@PathVariable String companyId, @PathVariable String codeId, RedirectAttributes redirectAttributes) {
        CodeId id = new CodeId(companyId, codeId);
        codeService.deleteCode(id);
        redirectAttributes.addFlashAttribute("message", "Code deleted successfully.");
        return "redirect:/common/code";
    }

    // CodeType 관련 메서드들
    @GetMapping("/type/new")
    public String typeForm(Model model) {
        model.addAttribute("codeType", new CodeType());
        return "common/code/type-form";
    }

    @PostMapping("/type")
    public String saveType(@ModelAttribute CodeType codeType, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        codeType.setCompanyId(userDetails.getCompanyId());
        codeService.saveCodeType(codeType);
        redirectAttributes.addFlashAttribute("message", "Code Type saved successfully.");
        return "redirect:/common/code";
    }

    @GetMapping("/type/{companyId}/{codeType}/edit")
    public String editTypeForm(@PathVariable String companyId, @PathVariable String codeType, Model model) {
        CodeTypeId id = new CodeTypeId(companyId, codeType);
        CodeType codeTypeEntity = codeService.getCodeTypeById(id);
        model.addAttribute("codeType", codeTypeEntity);
        return "common/code/type-form";
    }

    @PostMapping("/type/{companyId}/{codeType}")
    public String updateType(@PathVariable String companyId, @PathVariable String codeType, @ModelAttribute CodeType codeTypeEntity, RedirectAttributes redirectAttributes) {
        codeTypeEntity.setCompanyId(companyId);
        codeTypeEntity.setCodeType(codeType);
        codeService.saveCodeType(codeTypeEntity);
        redirectAttributes.addFlashAttribute("message", "Code Type updated successfully.");
        return "redirect:/common/code";
    }

    @PostMapping("/type/{companyId}/{codeType}/delete")
    public String deleteType(@PathVariable String companyId, @PathVariable String codeType, RedirectAttributes redirectAttributes) {
        CodeTypeId id = new CodeTypeId(companyId, codeType);
        codeService.deleteCodeType(id);
        redirectAttributes.addFlashAttribute("message", "Code Type deleted successfully.");
        return "redirect:/common/code";
    }
}
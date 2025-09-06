package com.cmms.workflow.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.workflow.entity.ApprovalRequest;
import com.cmms.workflow.entity.ApprovalRequestId;
import com.cmms.workflow.entity.ApprovalTemplate;
import com.cmms.workflow.entity.ApprovalTemplateId;
import com.cmms.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/workflow")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    // == Template Views ==
    @GetMapping("/templates")
    public String listTemplates(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, Pageable pageable) {
        Page<ApprovalTemplate> templates = workflowService.findTemplates(userDetails.getCompanyId(), pageable);
        model.addAttribute("templates", templates);
        return "workflow/template-list";
    }

    @GetMapping("/template/form")
    public String templateForm(Model model) {
        model.addAttribute("template", new ApprovalTemplate());
        return "workflow/template-form";
    }

    @PostMapping("/template/save")
    public String saveTemplate(@ModelAttribute ApprovalTemplate template, @AuthenticationPrincipal CustomUserDetails userDetails) {
        template.setCompanyId(userDetails.getCompanyId());
        workflowService.saveTemplate(template);
        return "redirect:/workflow/templates";
    }

    // == Request Views ==
    @GetMapping("/requests")
    public String listRequests(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, Pageable pageable) {
        Page<ApprovalRequest> requests = workflowService.findRequests(userDetails.getCompanyId(), pageable);
        model.addAttribute("requests", requests);
        return "workflow/request-list";
    }

    @GetMapping("/request/{approvalId}")
    public String requestDetail(@PathVariable String approvalId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        ApprovalRequestId id = new ApprovalRequestId(userDetails.getCompanyId(), approvalId);
        model.addAttribute("request", workflowService.findRequestById(id));
        return "workflow/request-detail";
    }
}

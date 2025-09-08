package com.cmms.domain.site.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.domain.site.entity.Site;
import com.cmms.domain.site.entity.SiteId;
import com.cmms.domain.site.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/domain/site")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("sites", siteService.getSitesByCompanyId(userDetails.getCompanyId()));
        return "domain/site/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("site", new Site());
        return "domain/site/form";
    }

    @GetMapping("/{siteId}/edit")
    public String editForm(@PathVariable String siteId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        SiteId id = new SiteId(userDetails.getCompanyId(), siteId);
        model.addAttribute("site", siteService.getSiteById(id));
        return "domain/site/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Site site, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        site.setCompanyId(userDetails.getCompanyId());
        try {
            siteService.saveSite(site);
            redirectAttributes.addFlashAttribute("message", "Site saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving site: " + e.getMessage());
            return "redirect:/domain/site/form";
        }
        return "redirect:/domain/site/list";
    }

    @PostMapping("/{siteId}/delete")
    public String delete(@PathVariable String siteId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        SiteId id = new SiteId(userDetails.getCompanyId(), siteId);
        try {
            siteService.deleteSite(id);
            redirectAttributes.addFlashAttribute("message", "Site deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting site: " + e.getMessage());
        }
        return "redirect:/domain/site/list";
    }
}
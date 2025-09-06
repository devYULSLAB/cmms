package com.cmms.storage.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.storage.entity.Storage;
import com.cmms.storage.entity.StorageId;
import com.cmms.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("storages", storageService.findAllByCompanyId(userDetails.getCompanyId()));
        return "storage/list";
    }

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("storage", new Storage());
        return "storage/form";
    }

    @GetMapping("/{storageId}/edit")
    public String editForm(@PathVariable String storageId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        StorageId id = new StorageId(userDetails.getCompanyId(), storageId);
        model.addAttribute("storage", storageService.findStorageById(id));
        return "storage/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Storage storage, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        storage.setCompanyId(userDetails.getCompanyId());
        try {
            storageService.saveStorage(storage);
            redirectAttributes.addFlashAttribute("message", "Storage location saved successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving storage location: " + e.getMessage());
            return "redirect:/storage/form";
        }
        return "redirect:/storage/list";
    }

    @PostMapping("/{storageId}/delete")
    public String delete(@PathVariable String storageId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        StorageId id = new StorageId(userDetails.getCompanyId(), storageId);
        try {
            storageService.deleteStorage(id);
            redirectAttributes.addFlashAttribute("message", "Storage location deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting storage location: " + e.getMessage());
        }
        return "redirect:/storage/list";
    }
}
package com.cmms.plant.controller;

import com.cmms.plant.entity.Plant;
import com.cmms.plant.service.PlantService;
import com.cmms.func.service.FuncService;
import com.cmms.domain.site.service.SiteService;
import com.cmms.domain.dept.service.DeptService;
import com.cmms.common.code.service.CodeService;
import com.cmms.common.file.service.FileAttachmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 프로그램명: CMMS 설비 관리 컨트롤러
 * 기능: 설비 등록, 조회, 수정, 삭제 화면 처리 및 파일 업로드 관리
 * 생성자: devYULSLAB
 * 생성일: 2025-02-28
 * 변경일: 2025-09-04
 */
@Controller
@RequestMapping("/plant")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;
    private final SiteService siteService;
    private final FuncService funcService;
    private final DeptService deptService;
    private final CodeService codeService;
    private final FileAttachmentService fileAttachmentService;

    @Value("${file.upload.max-file-count:10}")
    private int maxFileCount;

    @Value("${file.upload.max-file-size:10MB}")
    private String maxFileSize;

    @Value("${file.upload.accept:*/*}")
    private String fileAccept;

    @GetMapping("/form")
    public String form(Model model, HttpSession session) {
        String companyId = (String) session.getAttribute("companyId");

        Plant plant = new Plant();
        plant.setCompanyId(companyId);

        // fileGroupId 자동 생성 (10자리, 밀리초 기반)
        String fileGroupId = fileAttachmentService.createFieGroup(companyId);
        plant.setFileGroupId(fileGroupId);

        model.addAttribute("plant", plant);
        // Select box 데이터 추가
        model.addAttribute("sites", siteService.getSitesByCompanyId(companyId));
        model.addAttribute("funcs", funcService.getFuncsByCompanyId(companyId));
        model.addAttribute("depts", deptService.getDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", codeService.getCodesByCompanyIdAndCodeType(companyId, "ASSET"));
        model.addAttribute("depreMethods", codeService.getCodesByCompanyIdAndCodeType(companyId, "DEPRE"));

        // 파일 업로드 설정 추가
        model.addAttribute("maxFileCount", maxFileCount);
        model.addAttribute("maxFileSize", maxFileSize);
        model.addAttribute("fileAccept", fileAccept);

        return "plant/plantForm";
    }

    @GetMapping("/form/{siteId}/{plantId}")
    public String editForm(@PathVariable String siteId, @PathVariable String plantId,
            Model model,
            HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        Plant plant = plantService.getPlantById(new com.cmms.plant.entity.PlantId(companyId, plantId));

        // Select box 데이터 추가
        model.addAttribute("plant", plant);
        model.addAttribute("sites", siteService.getSitesByCompanyId(companyId));
        model.addAttribute("funcs", funcService.getFuncsByCompanyId(companyId));
        model.addAttribute("depts", deptService.getDeptsByCompanyId(companyId));
        model.addAttribute("assetTypes", codeService.getCodesByCompanyIdAndCodeType(companyId, "ASSET"));
        model.addAttribute("depreMethods", codeService.getCodesByCompanyIdAndCodeType(companyId, "DEPRE"));

        // 파일 업로드 설정 추가
        model.addAttribute("maxFileCount", maxFileCount);
        model.addAttribute("maxFileSize", maxFileSize);
        model.addAttribute("fileAccept", fileAccept);

        return "plant/plantForm";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Plant plant,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        plant.setCompanyId(companyId);

        plantService.savePlant(plant);

        return "redirect:/plant/list";
    }

    @GetMapping("/list")
    public String list(Model model,
            HttpSession session,
            @PageableDefault(size = 10, sort = "plantId") Pageable pageable) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        Page<Plant> plants = plantService.getPlantsByCompanyId(companyId, pageable);
        model.addAttribute("plants", plants);

        return "plant/plantList";
    }

    @GetMapping("/detail/{siteId}/{plantId}")
    public String detail(@PathVariable String siteId, @PathVariable String plantId,
            HttpSession session,
            Model model) {
        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        Plant plant = plantService.getPlantById(new com.cmms.plant.entity.PlantId(companyId, plantId));
        model.addAttribute("plant", plant);

        return "plant/plantDetail";
    }

    @PostMapping("/delete/{siteId}/{plantId}")
    public String delete(@PathVariable String siteId, @PathVariable String plantId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 세션에서 사용자 정보 가져오기
        String companyId = (String) session.getAttribute("companyId");

        try {
            plantService.deletePlant(new com.cmms.plant.entity.PlantId(companyId, plantId));
        } catch (Exception e) {
            throw new RuntimeException("삭제 중 오류 발생: " + e.getMessage());
        }
        return "redirect:/plant/list";
    }
}
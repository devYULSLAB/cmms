package com.yulslab.cmms.workorder.controller;

import com.yulslab.cmms.workorder.entity.Workorder;
import com.yulslab.cmms.workorder.entity.WorkorderId;
import com.yulslab.cmms.workorder.service.WorkorderService;
import com.yulslab.domain.common.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/workorder")
@RequiredArgsConstructor
public class WorkorderController {

    private final WorkorderService workorderService;
    private final DomainService domainService;

    @Value("${app.default-company-code}")
    private String defaultCompanyId;

    @GetMapping("/workorderList")
    public String workorderList(Model model) {
        List<Workorder> workorders = workorderService.findWorkorders();
        model.addAttribute("workorders", workorders);
        return "workorder/workorderList";
    }

    @GetMapping("/workorderForm")
    public String workorderForm(@RequestParam(required = false) String workorderId,
                                @RequestParam(required = false) String siteId,
                                Model model) {
        if (workorderId != null) {
            WorkorderId wId = new WorkorderId();
            wId.setCompanyId(defaultCompanyId);
            wId.setSiteId(siteId);
            wId.setWorkorderId(workorderId);
            workorderService.findById(wId).ifPresent(workorder -> model.addAttribute("workorder", workorder));
        } else {
            model.addAttribute("workorder", new Workorder());
        }

        model.addAttribute("sites", domainService.getSites(defaultCompanyId));
        // TODO: Add plants and inventories
        model.addAttribute("jobTypes", domainService.getCodes(defaultCompanyId, "JOBTP"));
        return "workorder/workorderForm";
    }

    @PostMapping("/save")
    public String saveWorkorder(@ModelAttribute("workorder") Workorder workorder) {
        if (workorder.getId() != null && workorder.getId().getCompanyId() == null) {
            workorder.getId().setCompanyId(defaultCompanyId);
        }
        workorderService.saveWorkorder(workorder);
        return "redirect:/workorder/workorderList";
    }
}

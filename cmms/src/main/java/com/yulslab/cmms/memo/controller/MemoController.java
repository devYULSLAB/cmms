package com.yulslab.cmms.memo.controller;

import com.yulslab.cmms.memo.entity.Memo;
import com.yulslab.cmms.memo.entity.MemoId;
import com.yulslab.cmms.memo.service.MemoService;
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
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final DomainService domainService;

    @Value("${app.default-company-code}")
    private String defaultCompanyId;

    @GetMapping("/memoList")
    public String memoList(Model model) {
        List<Memo> memos = memoService.findMemos();
        model.addAttribute("memos", memos);
        return "memo/memoList";
    }

    @GetMapping("/memoForm")
    public String memoForm(@RequestParam(required = false) String memoId, Model model) {
        if (memoId != null) {
            MemoId mId = new MemoId();
            mId.setCompanyId(defaultCompanyId);
            mId.setMemoId(memoId);
            memoService.findById(mId).ifPresent(memo -> model.addAttribute("memo", memo));
        } else {
            model.addAttribute("memo", new Memo());
        }
        // TODO: Add users for author dropdown
        return "memo/memoForm";
    }

    @PostMapping("/save")
    public String saveMemo(@ModelAttribute("memo") Memo memo) {
        if (memo.getId() != null && memo.getId().getCompanyId() == null) {
            memo.getId().setCompanyId(defaultCompanyId);
        }
        memoService.saveMemo(memo);
        return "redirect:/memo/memoList";
    }
}

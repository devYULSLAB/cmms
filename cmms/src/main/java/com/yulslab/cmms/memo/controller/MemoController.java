package com.yulslab.cmms.memo.controller;

import com.yulslab.cmms.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/memoList")
    public String memoList() {
        return "memo/memoList";
    }

    @GetMapping("/memoForm")
    public String memoForm() {
        return "memo/memoForm";
    }

    @GetMapping("/memoDetail")
    public String memoDetail() {
        return "memo/memoDetail";
    }
}

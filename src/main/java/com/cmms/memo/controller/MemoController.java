package com.cmms.memo.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.memo.entity.Memo;
import com.cmms.memo.entity.MemoComment;
import com.cmms.memo.entity.MemoCommentId;
import com.cmms.memo.entity.MemoId;
import com.cmms.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping
    public String list(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Memo> memos = memoService.getMemosByCompanyId(userDetails.getCompanyId(), pageable);
        model.addAttribute("memos", memos);
        return "memo/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("memo", new Memo());
        return "memo/form";
    }

    @PostMapping
    public String save(@ModelAttribute Memo memo, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        memo.setCompanyId(userDetails.getCompanyId());
        memo.setAuthorId(userDetails.getUsername());
        memoService.saveMemo(memo);
        redirectAttributes.addFlashAttribute("message", "Memo saved successfully.");
        return "redirect:/memo";
    }

    @GetMapping("/{memoId}")
    public String detail(@PathVariable String memoId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemoId id = new MemoId(userDetails.getCompanyId(), memoId);
        Memo memo = memoService.getMemoByIdAndIncrementViewCount(id);
        model.addAttribute("memo", memo);
        model.addAttribute("comments", memoService.getMemoCommentsByCompanyAndMemoId(id.getCompanyId(), id.getMemoId()));
        return "memo/detail";
    }

    @GetMapping("/{memoId}/edit")
    public String editForm(@PathVariable String memoId, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        MemoId id = new MemoId(userDetails.getCompanyId(), memoId);
        Memo memo = memoService.getMemoByIdAndIncrementViewCount(id); // Using same method to get data
        model.addAttribute("memo", memo);
        return "memo/form";
    }

    @PostMapping("/{memoId}")
    public String update(@PathVariable String memoId, @ModelAttribute Memo memo, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        memo.setCompanyId(userDetails.getCompanyId());
        memo.setMemoId(memoId);
        memo.setAuthorId(userDetails.getUsername());
        memoService.saveMemo(memo);
        redirectAttributes.addFlashAttribute("message", "Memo updated successfully.");
        return "redirect:/memo";
    }

    @PostMapping("/{memoId}/delete")
    public String delete(@PathVariable String memoId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        MemoId id = new MemoId(userDetails.getCompanyId(), memoId);
        memoService.deleteMemo(id);
        redirectAttributes.addFlashAttribute("message", "Memo deleted successfully.");
        return "redirect:/memo";
    }

    @PostMapping("/comment/save")
    public String saveComment(@ModelAttribute MemoComment comment, @RequestParam String memoId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        comment.setCompanyId(userDetails.getCompanyId());
        comment.setMemoId(memoId);
        comment.setAuthorId(userDetails.getUsername());
        memoService.saveMemoComment(comment);
        return "redirect:/memo/" + memoId;
    }

    @PostMapping("/comment/{commentId}/delete")
    public String deleteComment(@PathVariable String commentId, @RequestParam String memoId, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes) {
        MemoCommentId id = new MemoCommentId(userDetails.getCompanyId(), memoId, commentId);
        memoService.deleteMemoComment(id);
        return "redirect:/memo/" + memoId;
    }
}

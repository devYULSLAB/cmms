package com.cmms.memo.service;

import com.cmms.common.id.IdGeneratorService;
import com.cmms.memo.entity.Memo;
import com.cmms.memo.entity.MemoComment;
import com.cmms.memo.entity.MemoCommentId;
import com.cmms.memo.entity.MemoId;
import com.cmms.memo.repository.MemoCommentRepository;
import com.cmms.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final MemoCommentRepository memoCommentRepository;
    private final IdGeneratorService idGeneratorService;

    @Transactional
    public Memo saveMemo(Memo memo) {
        if (memo.getMemoId() == null || memo.getMemoId().isEmpty()) {
            memo.setMemoId(idGeneratorService.generateId(memo.getCompanyId(), "0"));
            memo.setViewCnt(0);
        }
        return memoRepository.save(memo);
    }

    @Transactional(readOnly = true)
    public Page<Memo> getMemosByCompanyId(String companyId, Pageable pageable) {
        return memoRepository.findMemosByCompanyId(companyId, pageable);
    }

    @Transactional
    public Memo getMemoByIdAndIncrementViewCount(MemoId memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found: " + memoId));
        memo.setViewCnt(memo.getViewCnt() + 1);
        return memoRepository.save(memo);
    }

    @Transactional
    public void deleteMemo(MemoId memoId) {
        if (!memoRepository.existsById(memoId)) {
            throw new RuntimeException("Memo not found with ID: " + memoId);
        }
        memoCommentRepository.deleteAll(getMemoCommentsByCompanyAndMemoId(memoId.getCompanyId(), memoId.getMemoId()));
        memoRepository.deleteById(memoId);
    }

    @Transactional(readOnly = true)
    public List<MemoComment> getMemoCommentsByCompanyAndMemoId(String companyId, String memoId) {
        return memoCommentRepository.findByCompanyIdAndMemoIdOrderByCreateDateAsc(companyId, memoId);
    }

    @Transactional
    public MemoComment saveMemoComment(MemoComment comment) {
        if (comment.getCommentId() == null || comment.getCommentId().isEmpty()) {
            comment.setCommentId(idGeneratorService.generateId(comment.getCompanyId(), "0"));
        }
        return memoCommentRepository.save(comment);
    }

    @Transactional
    public void deleteMemoComment(MemoCommentId commentId) {
        memoCommentRepository.deleteById(commentId);
    }
}
package com.yulslab.cmms.memo.service;

import com.yulslab.cmms.common.IdGenerationService;
import com.yulslab.cmms.memo.entity.Memo;
import com.yulslab.cmms.memo.entity.MemoId;
import com.yulslab.cmms.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {
    private final MemoRepository memoRepository;
    private final IdGenerationService idGenerationService;

    public List<Memo> findMemos() {
        return memoRepository.findAll();
    }

    public Optional<Memo> findById(MemoId memoId) {
        return memoRepository.findById(memoId);
    }

    @Transactional
    public Memo saveMemo(Memo memo) {
        if (memo.getId() == null || memo.getId().getMemoId() == null || memo.getId().getMemoId().isEmpty()) {
            String nextId = idGenerationService.generateNextId('8'); // '8' for memo
            if (memo.getId() == null) {
                memo.setId(new MemoId());
            }
            memo.getId().setMemoId(nextId);
        }
        return memoRepository.save(memo);
    }
}

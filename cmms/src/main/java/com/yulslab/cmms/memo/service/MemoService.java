package com.yulslab.cmms.memo.service;

import com.yulslab.cmms.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    // Business logic for memo management
}

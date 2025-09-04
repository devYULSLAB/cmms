package com.yulslab.cmms.memo.repository;

import com.yulslab.cmms.memo.entity.Memo;
import com.yulslab.cmms.memo.entity.MemoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, MemoId> {
}

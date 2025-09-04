package com.yulslab.cmms.memo.repository;

import com.yulslab.cmms.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}

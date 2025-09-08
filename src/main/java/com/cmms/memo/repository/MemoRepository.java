package com.cmms.memo.repository;

import com.cmms.memo.entity.Memo;
import com.cmms.memo.entity.MemoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo, MemoId> {

    Page<Memo> findMemosByCompanyId(String companyId, Pageable pageable);

    Page<Memo> findMemosByCompanyIdAndMemoNameContaining(String companyId, String memoName, Pageable pageable);

    Page<Memo> findMemosByCompanyIdAndAuthorId(String companyId, String authorId, Pageable pageable);
}

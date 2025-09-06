package com.cmms.memo.repository;

import com.cmms.memo.entity.Memo;
import com.cmms.memo.entity.MemoId;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo, MemoId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT MAX(m.memoId) FROM Memo m WHERE m.companyId = :companyId")
    String findMaxMemoIdByCompanyId(@Param("companyId") String companyId);

    Page<Memo> findByCompanyId(String companyId, Pageable pageable);

    Page<Memo> findByCompanyIdAndMemoNameContaining(String companyId, String memoName, Pageable pageable);

    Page<Memo> findByCompanyIdAndAuthorId(String companyId, String authorId, Pageable pageable);
}

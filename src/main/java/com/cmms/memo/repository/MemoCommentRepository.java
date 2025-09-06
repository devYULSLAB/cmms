package com.cmms.memo.repository;

import com.cmms.memo.entity.MemoComment;
import com.cmms.memo.entity.MemoCommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoCommentRepository extends JpaRepository<MemoComment, MemoCommentId> {

    @Query("SELECT MAX(m.commentId) FROM MemoComment m WHERE m.companyId = :companyId AND m.memoId = :memoId")
    String findMaxCommentIdByCompanyIdAndMemoId(
            @Param("companyId") String companyId,
            @Param("memoId") String memoId
    );

    List<MemoComment> findByCompanyIdAndMemoIdOrderByCreateDateAsc(String companyId, String memoId);

    long countByCompanyIdAndMemoId(String companyId, String memoId);
}

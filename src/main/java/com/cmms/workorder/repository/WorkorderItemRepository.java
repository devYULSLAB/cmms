package com.cmms.workorder.repository;

import com.cmms.workorder.entity.WorkorderItem;
import com.cmms.workorder.entity.WorkorderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkorderItemRepository extends JpaRepository<WorkorderItem, WorkorderItemId> {

    @Query("SELECT MAX(w.itemId) FROM WorkorderItem w WHERE w.companyId = :companyId AND w.workorderId = :workorderId")
    String findMaxItemIdByCompanyIdAndWorkorderId(
            @Param("companyId") String companyId,
            @Param("workorderId") String workorderId
    );

    List<WorkorderItem> findByCompanyIdAndWorkorderIdOrderByItemIdAsc(String companyId, String workorderId);
}

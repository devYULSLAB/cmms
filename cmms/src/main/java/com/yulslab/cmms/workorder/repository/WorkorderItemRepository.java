package com.yulslab.cmms.workorder.repository;

import com.yulslab.cmms.workorder.entity.WorkorderItem;
import com.yulslab.cmms.workorder.entity.WorkorderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkorderItemRepository extends JpaRepository<WorkorderItem, WorkorderItemId> {
}

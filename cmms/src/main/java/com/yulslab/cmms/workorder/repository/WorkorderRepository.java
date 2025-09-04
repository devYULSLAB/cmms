package com.yulslab.cmms.workorder.repository;

import com.yulslab.cmms.workorder.entity.Workorder;
import com.yulslab.cmms.workorder.entity.WorkorderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkorderRepository extends JpaRepository<Workorder, WorkorderId> {
}

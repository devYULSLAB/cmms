package com.cmms.domain.dept.repository;

import com.cmms.domain.dept.entity.Dept;
import com.cmms.domain.dept.entity.DeptId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeptRepository extends JpaRepository<Dept, DeptId> {

    List<Dept> findDeptsByCompanyId(String companyId);
    
    Optional<Dept> findDeptByCompanyIdAndDeptId(String companyId, String deptId);
}

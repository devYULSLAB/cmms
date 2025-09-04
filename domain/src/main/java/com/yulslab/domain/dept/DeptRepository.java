package com.yulslab.domain.dept;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeptRepository extends JpaRepository<Dept, DeptId> {
    List<Dept> findById_CompanyIdAndUseYn(String companyId, char useYn);
}

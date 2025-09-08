package com.cmms.domain.dept.service;

import com.cmms.domain.dept.entity.Dept;
import com.cmms.domain.dept.entity.DeptId;
import com.cmms.domain.dept.repository.DeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DeptService {

    private final DeptRepository deptRepository;

    @Transactional(readOnly = true)
    public List<Dept> getDeptsByCompanyId(String companyId) {
        return deptRepository.findDeptsByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Dept getDeptById(DeptId deptId) {
        return deptRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Dept not found"));
    }

    @Transactional(readOnly = true)
    public Dept getDeptByCompanyIdAndDeptId(String companyId, String deptId) {
        return deptRepository.findDeptByCompanyIdAndDeptId(companyId, deptId)
                .orElseThrow(() -> new RuntimeException("Dept not found: " + deptId));
    }

    public Dept saveDept(Dept dept) {
        return deptRepository.save(dept);
    }

    public void deleteDept(DeptId deptId) {
        deptRepository.deleteById(deptId);
    }
}
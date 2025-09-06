package com.cmms.common.code.repository;

import com.cmms.common.code.entity.CodeType;
import com.cmms.common.code.entity.CodeTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeTypeRepository extends JpaRepository<CodeType, CodeTypeId> {
    List<CodeType> findByCompanyId(String companyId);
}

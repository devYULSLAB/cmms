package com.cmms.common.code.repository;

import com.cmms.common.code.entity.Code;
import com.cmms.common.code.entity.CodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, CodeId> {

    List<Code> findByCompanyId(String companyId);

    List<Code> findByCompanyIdAndCodeType(String companyId, String codeType);
}

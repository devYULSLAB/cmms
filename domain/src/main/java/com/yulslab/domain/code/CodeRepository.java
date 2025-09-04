package com.yulslab.domain.code;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CodeRepository extends JpaRepository<Code, CodeId> {
    List<Code> findById_CompanyIdAndCodeTypeAndUseYn(String companyId, String codeType, char useYn);
}

package com.yulslab.domain.func;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FuncRepository extends JpaRepository<Func, FuncId> {
    List<Func> findById_CompanyIdAndUseYn(String companyId, char useYn);
}

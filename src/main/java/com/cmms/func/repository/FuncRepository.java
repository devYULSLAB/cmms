package com.cmms.func.repository;

import com.cmms.func.entity.Func;
import com.cmms.func.entity.FuncId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncRepository extends JpaRepository<Func, FuncId> {

    List<Func> findByCompanyId(String companyId);
}
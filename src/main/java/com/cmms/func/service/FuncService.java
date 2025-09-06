package com.cmms.func.service;

import com.cmms.func.entity.Func;
import com.cmms.func.entity.FuncId;
import com.cmms.func.repository.FuncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FuncService {

    private final FuncRepository funcRepository;

    @Transactional(readOnly = true)
    public List<Func> findAllByCompanyId(String companyId) {
        return funcRepository.findByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Func findFuncById(FuncId id) {
        return funcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Function location not found"));
    }

    public Func saveFunc(Func func) {
        return funcRepository.save(func);
    }

    public void deleteFunc(FuncId id) {
        funcRepository.deleteById(id);
    }
}
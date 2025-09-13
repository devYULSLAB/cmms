package com.cmms.common.code.service;

import com.cmms.common.code.entity.Code;
import com.cmms.common.code.entity.CodeId;
import com.cmms.common.code.entity.CodeType;
import com.cmms.common.code.entity.CodeTypeId;
import com.cmms.common.code.repository.CodeRepository;
import com.cmms.common.code.repository.CodeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CodeService {

    private final CodeRepository codeRepository;
    private final CodeTypeRepository codeTypeRepository;

    // == Code Methods ==

    @Transactional(readOnly = true)
    public List<Code> getCodesByCompanyIdAndCodeType(String companyId, String codeType) {
        return codeRepository.findByCompanyIdAndCodeType(companyId, codeType);
    }

    public Code saveCode(Code code) {
        return codeRepository.save(code);
    }

    @Transactional(readOnly = true)
    public Code getCodeById(CodeId id) {
        return codeRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public CodeType getCodeTypeById(CodeTypeId id) {
        return codeTypeRepository.findById(id).orElse(null);
    }

    public void deleteCode(CodeId id) {
        codeRepository.deleteById(id);
    }

    // == CodeType Methods ==
    @Transactional(readOnly = true)
    public List<CodeType> getCodeTypesByCompany(String companyId) {
        return codeTypeRepository.findByCompanyId(companyId);
    }

    public CodeType saveCodeType(CodeType codeType) {
        return codeTypeRepository.save(codeType);
    }

    public void deleteCodeType(CodeTypeId id) {
        codeTypeRepository.deleteById(id);
    }
}
package com.yulslab.domain.common;

import com.yulslab.domain.code.Code;
import com.yulslab.domain.code.CodeRepository;
import com.yulslab.domain.dept.Dept;
import com.yulslab.domain.dept.DeptRepository;
import com.yulslab.domain.site.Site;
import com.yulslab.domain.site.SiteRepository;
import com.yulslab.domain.func.Func;
import com.yulslab.domain.func.FuncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DomainService {

    private final SiteRepository siteRepository;
    private final DeptRepository deptRepository;
    private final FuncRepository funcRepository;
    private final CodeRepository codeRepository;

    public List<Site> getSites(String companyId) {
        return siteRepository.findById_CompanyIdAndUseYn(companyId, 'Y');
    }

    public List<Dept> getDepts(String companyId) {
        return deptRepository.findById_CompanyIdAndUseYn(companyId, 'Y');
    }

    public List<Func> getFuncs(String companyId) {
        return funcRepository.findById_CompanyIdAndUseYn(companyId, 'Y');
    }

    public List<Code> getCodes(String companyId, String codeType) {
        return codeRepository.findById_CompanyIdAndCodeTypeAndUseYn(companyId, codeType, 'Y');
    }
}

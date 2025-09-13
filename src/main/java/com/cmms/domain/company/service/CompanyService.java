package com.cmms.domain.company.service;

import com.cmms.domain.company.entity.Company;
import com.cmms.domain.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(String companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    @Transactional(readOnly = true)
    public Company findCompanyByCompanyId(String companyId) {
        return companyRepository.findCompanyByCompanyId(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found: " + companyId));
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public void deleteCompany(String companyId) {
        companyRepository.deleteById(companyId);
    }
}
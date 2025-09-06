package com.cmms.domain.company.repository;

import com.cmms.domain.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    // Basic CRUD methods are inherited from JpaRepository
}

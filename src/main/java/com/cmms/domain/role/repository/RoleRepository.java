package com.cmms.domain.role.repository;

import com.cmms.domain.role.entity.Role;
import com.cmms.domain.role.entity.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleId> {
    List<Role> findByCompanyId(String companyId);
}

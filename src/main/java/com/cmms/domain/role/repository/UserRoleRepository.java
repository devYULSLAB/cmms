package com.cmms.domain.role.repository;

import com.cmms.domain.role.entity.UserRole;
import com.cmms.domain.role.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByCompanyIdAndUserId(String companyId, String userId);
    List<UserRole> findByCompanyIdAndRoleId(String companyId, String roleId);
}

package com.cmms.domain.userrole.repository;

import com.cmms.domain.userrole.entity.UserRole;
import com.cmms.domain.userrole.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRole Repository
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    /**
     * 특정 사용자의 모든 역할 조회
     */
    List<UserRole> findUserRolesByCompanyIdAndUserId(String companyId, String userId);

    /**
     * 특정 역할을 가진 모든 사용자 조회
     */
    List<UserRole> findUserRolesByCompanyIdAndRoleId(String companyId, String roleId);

    /**
     * 사용자 역할 삭제
     */
    void deleteByCompanyIdAndUserIdAndRoleId(String companyId, String userId, String roleId);
}

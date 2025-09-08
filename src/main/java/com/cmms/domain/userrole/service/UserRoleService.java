package com.cmms.domain.userrole.service;

import com.cmms.domain.userrole.entity.UserRole;
import com.cmms.domain.userrole.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UserRole Service
 */
@Service
@Transactional
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * 사용자의 모든 역할 조회
     */
    @Transactional(readOnly = true)
    public List<UserRole> getUserRoles(String companyId, String userId) {
        return userRoleRepository.findUserRolesByCompanyIdAndUserId(companyId, userId);
    }

    /**
     * 역할의 모든 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<UserRole> getRoleUsers(String companyId, String roleId) {
        return userRoleRepository.findUserRolesByCompanyIdAndRoleId(companyId, roleId);
    }

    /**
     * 사용자에게 역할 부여
     */
    public UserRole assignRole(String companyId, String userId, String roleId) {
        UserRole userRole = new UserRole(companyId, userId, roleId);
        return userRoleRepository.save(userRole);
    }

    /**
     * 사용자에서 역할 제거
     */
    public void removeRole(String companyId, String userId, String roleId) {
        userRoleRepository.deleteByCompanyIdAndUserIdAndRoleId(companyId, userId, roleId);
    }

    /**
     * 사용자의 모든 역할 제거
     */
    public void removeAllUserRoles(String companyId, String userId) {
        List<UserRole> userRoles = getUserRoles(companyId, userId);
        userRoleRepository.deleteAll(userRoles);
    }
}

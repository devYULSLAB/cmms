package com.cmms.domain.role.service;

import com.cmms.domain.role.entity.Role;
import com.cmms.domain.role.entity.RoleId;
import com.cmms.domain.role.entity.UserRole;
import com.cmms.domain.role.entity.UserRoleId;
import com.cmms.domain.role.repository.RoleRepository;
import com.cmms.domain.role.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    // == Role Methods ==
    @Transactional(readOnly = true)
    public List<Role> getRolesByCompanyId(String companyId) {
        return roleRepository.findRolesByCompanyId(companyId);
    }

    @Transactional(readOnly = true)
    public Role getRoleById(RoleId id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(RoleId id) {
        roleRepository.deleteById(id);
    }

    // == UserRole Methods ==
    @Transactional(readOnly = true)
    public List<UserRole> getUserRolesByCompanyAndUserId(String companyId, String userId) {
        return userRoleRepository.findByCompanyIdAndUserId(companyId, userId);
    }

    public UserRole saveUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    public void deleteUserRole(UserRoleId id) {
        userRoleRepository.deleteById(id);
    }
}

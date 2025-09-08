package com.cmms.domain.role.service;

import com.cmms.domain.role.entity.Role;
import com.cmms.domain.role.entity.RoleId;
import com.cmms.domain.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

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

}

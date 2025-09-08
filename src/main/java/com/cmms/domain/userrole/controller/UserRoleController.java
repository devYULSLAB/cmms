package com.cmms.domain.userrole.controller;

import com.cmms.domain.userrole.entity.UserRole;
import com.cmms.domain.userrole.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserRole Controller
 */
@RestController
@RequestMapping("/api/userrole")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 사용자의 모든 역할 조회
     */
    @GetMapping("/user/{companyId}/{userId}")
    public ResponseEntity<List<UserRole>> getUserRoles(
            @PathVariable String companyId,
            @PathVariable String userId) {
        List<UserRole> userRoles = userRoleService.getUserRoles(companyId, userId);
        return ResponseEntity.ok(userRoles);
    }

    /**
     * 역할의 모든 사용자 조회
     */
    @GetMapping("/role/{companyId}/{roleId}")
    public ResponseEntity<List<UserRole>> getRoleUsers(
            @PathVariable String companyId,
            @PathVariable String roleId) {
        List<UserRole> roleUsers = userRoleService.getRoleUsers(companyId, roleId);
        return ResponseEntity.ok(roleUsers);
    }

    /**
     * 사용자에게 역할 부여
     */
    @PostMapping("/assign")
    public ResponseEntity<UserRole> assignRole(@RequestBody UserRole userRole) {
        UserRole assignedRole = userRoleService.assignRole(
                userRole.getCompanyId(),
                userRole.getUserId(),
                userRole.getRoleId()
        );
        return ResponseEntity.ok(assignedRole);
    }

    /**
     * 사용자에서 역할 제거
     */
    @DeleteMapping("/remove/{companyId}/{userId}/{roleId}")
    public ResponseEntity<Void> removeRole(
            @PathVariable String companyId,
            @PathVariable String userId,
            @PathVariable String roleId) {
        userRoleService.removeRole(companyId, userId, roleId);
        return ResponseEntity.ok().build();
    }
}

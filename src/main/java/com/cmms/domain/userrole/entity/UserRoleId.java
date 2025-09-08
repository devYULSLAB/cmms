package com.cmms.domain.userrole.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UserRole 복합키 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {

    private String companyId;
    private String userId;
    private String roleId;
}

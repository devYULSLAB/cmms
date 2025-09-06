package com.cmms.domain.role.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleId implements Serializable {
    private String companyId;
    private String roleId;
}

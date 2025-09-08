package com.cmms.domain.dept.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptId implements Serializable {
    private String companyId;
    private String deptId;
}
package com.yulslab.domain.dept;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class DeptId implements Serializable {
    private String companyId;
    private String deptId;
}

package com.yulslab.domain.dept;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dept")
@Getter
@Setter
public class Dept {
    @EmbeddedId
    private DeptId id;
    private String deptName;
    private char useYn;
}

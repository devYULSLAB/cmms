package com.cmms.domain.dept.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dept")
@IdClass(DeptIdClass.class)
@DynamicInsert
@DynamicUpdate
public class Dept {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "dept_id", length = 5, nullable = false)
    private String deptId;

    @Column(name = "dept_name", length = 100, nullable = false)
    private String deptName;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}

package com.cmms.domain.role.entity;

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
@Table(name = "role")
@IdClass(RoleId.class)
@DynamicInsert
@DynamicUpdate
public class Role {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "role_id", length = 5, nullable = false)
    private String roleId;

    @Column(name = "role_name", length = 100, nullable = false)
    private String roleName;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}

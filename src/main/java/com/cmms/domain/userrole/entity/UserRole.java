package com.cmms.domain.userrole.entity;

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
@Table(name = "user_role")
@IdClass(UserRoleId.class)
@DynamicInsert
@DynamicUpdate
public class UserRole {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "user_id", length = 5, nullable = false)
    private String userId;

    @Id
    @Column(name = "role_id", length = 5, nullable = false)
    private String roleId;

    @Column(name = "grant_date", nullable = false, updatable = false)
    private LocalDateTime grantDate;
}

package com.cmms.domain.userrole.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * UserRole 엔티티
 * 사용자와 역할의 다대다 관계를 나타내는 중간 테이블
 */
@Entity
@Table(name = "user_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserRoleId.class)
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

    @Column(name = "grant_date", nullable = false)
    private LocalDateTime grantDate;

    // 생성자 (ID 필드만)
    public UserRole(String companyId, String userId, String roleId) {
        this.companyId = companyId;
        this.userId = userId;
        this.roleId = roleId;
        this.grantDate = LocalDateTime.now();
    }
}

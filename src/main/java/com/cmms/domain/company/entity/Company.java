package com.cmms.domain.company.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "company")
@DynamicInsert
@DynamicUpdate
public class Company {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Column(name = "company_name", length = 100, nullable = false)
    private String companyName;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}

package com.cmms.common.code.entity;

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
@Table(name = "code")
@IdClass(CodeId.class)
@DynamicInsert
@DynamicUpdate
public class Code {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "code_id", length = 5, nullable = false)
    private String codeId;

    @Column(name = "code_type", length = 20, nullable = false)
    private String codeType;

    @Column(name = "code_name", length = 100, nullable = false)
    private String codeName;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}

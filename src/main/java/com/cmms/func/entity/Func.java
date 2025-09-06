package com.cmms.func.entity;

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
@Table(name = "func")
@IdClass(FuncId.class)
@DynamicInsert
@DynamicUpdate
public class Func {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "func_id", length = 5, nullable = false)
    private String funcId;

    @Column(name = "func_name", length = 100, nullable = false)
    private String funcName;

    @Column(name = "parent_func_id", length = 5)
    private String parentFuncId;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}
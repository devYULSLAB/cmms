package com.cmms.workpermit.entity;

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
@Table(name = "workpermit_item")
@IdClass(WorkpermitItemIdClass.class)
@DynamicInsert
@DynamicUpdate
public class WorkpermitItem {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "permit_id", length = 10, nullable = false)
    private String permitId;

    @Id
    @Column(name = "item_id", length = 2, nullable = false)
    private String itemId;

    @Column(name = "item_name", length = 100, nullable = false)
    private String itemName;

    @Column(name = "signature", length = 100)
    private String signature;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "company_id", referencedColumnName = "company_id", insertable = false, updatable = false),
            @JoinColumn(name = "permit_id", referencedColumnName = "permit_id", insertable = false, updatable = false)
    })
    private Workpermit workpermit;
}

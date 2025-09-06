package com.cmms.storage.entity;

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
@Table(name = "storage")
@IdClass(StorageId.class)
@DynamicInsert
@DynamicUpdate
public class Storage {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "storage_id", length = 5, nullable = false)
    private String storageId;

    @Column(name = "storage_name", length = 100, nullable = false)
    private String storageName;

    @Column(name = "parent_storage_id", length = 5)
    private String parentStorageId;

    @Column(name = "use_yn", length = 1, nullable = false)
    private String useYn;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;
}
package com.cmms.common.file.entity;

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
@Table(name = "file_group")
@IdClass(FileGroupId.class)
@DynamicInsert
@DynamicUpdate
public class FileGroup {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "file_group_id", length = 10, nullable = false)
    private String fileGroupId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;
}

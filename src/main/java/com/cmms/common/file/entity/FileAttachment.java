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
@Table(name = "file_attach")
@DynamicInsert
@DynamicUpdate
public class FileAttachment {

    @EmbeddedId
    private FileAttachmentId id;

    @Column(name = "company_id", length = 5, nullable = false, insertable = false, updatable = false)
    private String companyId;

    @Column(name = "file_group_id", length = 10, nullable = false)
    private String fileGroupId;

    @Column(name = "original_name", length = 255, nullable = false)
    private String originalName;

    @Column(name = "stored_name", length = 255, nullable = false)
    private String storedName;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "storage_path", length = 500)
    private String storagePath;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;
}

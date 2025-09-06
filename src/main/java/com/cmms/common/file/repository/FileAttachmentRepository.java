package com.cmms.common.file.repository;

import com.cmms.common.file.entity.FileAttachment;
import com.cmms.common.file.entity.FileAttachmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, FileAttachmentId> {

    List<FileAttachment> findByCompanyIdAndFileGroupId(String companyId, String fileGroupId);

    void deleteByCompanyIdAndFileGroupId(String companyId, String fileGroupId);
}

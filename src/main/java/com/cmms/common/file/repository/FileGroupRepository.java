package com.cmms.common.file.repository;

import com.cmms.common.file.entity.FileGroup;
import com.cmms.common.file.entity.FileGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileGroupRepository extends JpaRepository<FileGroup, FileGroupId> {
}

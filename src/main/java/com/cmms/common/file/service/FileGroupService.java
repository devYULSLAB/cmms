package com.cmms.common.file.service;

import com.cmms.common.file.entity.FileGroup;
import com.cmms.common.file.repository.FileGroupRepository;
import com.cmms.common.id.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileGroupService {

    private final FileGroupRepository fileGroupRepository;
    private final IdGeneratorService idGeneratorService;

    public FileGroup saveFileGroup(FileGroup fileGroup) {
        if (fileGroup.getFileGroupId() == null || fileGroup.getFileGroupId().isEmpty()) {
            fileGroup.setFileGroupId(idGeneratorService.generateFileGroupId());
        }
        return fileGroupRepository.save(fileGroup);
    }
}

package com.cmms.common.file.service;

import com.cmms.common.file.entity.FileAttachment;
import com.cmms.common.file.entity.FileAttachmentId;
import com.cmms.common.file.entity.FileGroup;
import com.cmms.common.file.repository.FileAttachmentRepository;
import com.cmms.common.file.repository.FileGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.cmms.common.id.IdGeneratorService;

@Service
@RequiredArgsConstructor
@Transactional
public class FileAttachmentService {

    private final FileAttachmentRepository fileAttachmentRepository;
    private final FileGroupRepository fileGroupRepository;
    private final IdGeneratorService idGeneratorService;

    @Value("${file.upload.root-path:uploads}")
    private String uploadRootPath;

    public String createFieGroup(String companyId) {
        String fileGroupId = idGeneratorService.generateFileGroupId();
        FileGroup fileGroup = new FileGroup();
        fileGroup.setCompanyId(companyId);
        fileGroup.setFileGroupId(fileGroupId);
        fileGroupRepository.save(fileGroup);
        return fileGroupId;
    }

    public FileAttachment storeFile(MultipartFile file, String companyId, String fileGroupId, String module) throws IOException {
        String fileId = idGeneratorService.generateFileId();
        String originalName = file.getOriginalFilename();
        String storedName = fileId + getFileExtension(originalName);

        LocalDate today = LocalDate.now();
        String datePath = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path destinationPath = Paths.get(uploadRootPath, companyId, module, datePath).normalize();
        Files.createDirectories(destinationPath);

        Path targetLocation = destinationPath.resolve(storedName);
        Files.copy(file.getInputStream(), targetLocation);

        FileAttachment attachment = new FileAttachment();
        attachment.setId(new FileAttachmentId(companyId, fileId));
        attachment.setFileGroupId(fileGroupId);
        attachment.setOriginalName(originalName);
        attachment.setStoredName(storedName);
        attachment.setMimeType(file.getContentType());
        attachment.setFileSize(file.getSize());
        attachment.setStoragePath(targetLocation.toString());

        return fileAttachmentRepository.save(attachment);
    }

    @Transactional(readOnly = true)
    public List<FileAttachment> getFilesByGroup(String companyId, String fileGroupId) {
        return fileAttachmentRepository.findByCompanyIdAndFileGroupId(companyId, fileGroupId);
    }

    public void deleteFile(FileAttachmentId id) throws IOException {
        FileAttachment attachment = fileAttachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        Files.deleteIfExists(Paths.get(attachment.getStoragePath()));
        fileAttachmentRepository.deleteById(id);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}

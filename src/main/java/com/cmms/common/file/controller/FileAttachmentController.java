package com.cmms.common.file.controller;

import com.cmms.auth.dto.CustomUserDetails;
import com.cmms.common.file.entity.FileAttachment;
import com.cmms.common.file.entity.FileAttachmentId;
import com.cmms.common.file.service.FileAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileAttachmentController {

    private final FileAttachmentService fileAttachmentService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("fileGroupId") String fileGroupId,
                                        @RequestParam("moduleName") String moduleName,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            FileAttachment attachment = fileAttachmentService.storeFile(file, userDetails.getCompanyId(), fileGroupId, moduleName);
            return ResponseEntity.ok(Map.of("success", true, "fileData", Map.of(
                "fileId", attachment.getId().getFileId(),
                "originalFileName", attachment.getOriginalName(),
                "fileSize", attachment.getFileSize()
            )));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to upload file."));
        }
    }

    @GetMapping("/list/{fileGroupId}")
    @ResponseBody
    public ResponseEntity<List<FileAttachment>> getFiles(@PathVariable String fileGroupId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<FileAttachment> files = fileAttachmentService.getFilesByGroup(userDetails.getCompanyId(), fileGroupId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        FileAttachmentId id = new FileAttachmentId(userDetails.getCompanyId(), fileId);
        try {
            FileAttachment fileAttachment = fileAttachmentService.getFileById(id);
            Path filePath = Paths.get(fileAttachment.getStoragePath());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileAttachment.getOriginalName() + "\"")
                        .contentType(MediaType.parseMediaType(fileAttachment.getMimeType()))
                        .body(resource);
            }
        } catch (MalformedURLException e) {
            // ignore
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{fileId}")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String fileId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        FileAttachmentId id = new FileAttachmentId(userDetails.getCompanyId(), fileId);
        try {
            fileAttachmentService.deleteFile(id);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

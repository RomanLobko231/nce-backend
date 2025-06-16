package com.nce.backend.filestorage;

import com.nce.backend.filestorage.domain.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageFacade {

    private final FileStorageService<MultipartFile> fileStorageService;

    public String uploadFile(MultipartFile file) {
        return fileStorageService.uploadFile(file);
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        return fileStorageService.uploadFiles(files);
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null) {
            return;
        }
        fileStorageService.deleteFile(fileUrl);
    }

    public void deleteFiles(List<String> fileUrls) {
        if (fileUrls == null || fileUrls.isEmpty()) {
            return;
        }
        fileStorageService.deleteFiles(fileUrls);
    }

    public String generatePresignedUrl(String fileUrl) {
        return fileStorageService.generatePresignedUrl(fileUrl);
    }

}

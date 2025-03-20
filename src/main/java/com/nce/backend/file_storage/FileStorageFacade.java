package com.nce.backend.file_storage;

import com.nce.backend.file_storage.domain.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageFacade {

    private final FileStorageService<MultipartFile> fileStorageService;

    public String uploadFile(MultipartFile file) {
        return fileStorageService.uploadFile(file);
    }

    public List<String> uploadFiles(List<MultipartFile> files) {
       return fileStorageService.uploadFiles(files);
    }

    public void deleteFile(String fileUrl) {
        fileStorageService.deleteFile(fileUrl);
    }

    public void deleteFiles(List<String> fileUrls) {
        fileStorageService.deleteFiles(fileUrls);
    }
}

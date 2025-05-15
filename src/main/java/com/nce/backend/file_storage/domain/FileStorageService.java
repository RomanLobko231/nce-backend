package com.nce.backend.file_storage.domain;

import java.util.List;

public interface FileStorageService<T> {
    String uploadFile(T file);
    List<String> uploadFiles(List<T> files);
    void deleteFile(String fileUrl);
    void deleteFiles(List<String> fileUrls);
    String generatePresignedUrl(String key);
}

package com.nce.backend.filestorage.domain;

import java.util.List;

public interface FileStorageService<T> {
    String uploadFile(T file);
    List<String> uploadFiles(List<T> files);
    void deleteFile(String fileUrl);
    void deleteFiles(List<String> fileUrls);
    String generatePresignedUrl(String key);
}

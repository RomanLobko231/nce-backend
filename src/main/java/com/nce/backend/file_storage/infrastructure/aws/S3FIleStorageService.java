package com.nce.backend.file_storage.infrastructure.aws;

import com.nce.backend.file_storage.domain.FileStorageService;
import com.nce.backend.common.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3FIleStorageService implements FileStorageService<MultipartFile> {

    private final S3Client s3Client;

    private final String BUCKET_NAME = "maro-nalo";

    @Override
    public String uploadFile(MultipartFile file) {
        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(uniqueFileName)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getInputStream().available()));
        } catch (IOException e) {
            throw new FileProcessingException(
                    "File '%s' could not be processed".formatted(file.getOriginalFilename())
            );
        }

        return s3Client.utilities().getUrl(builder -> builder.bucket(BUCKET_NAME).key(uniqueFileName)).toExternalForm();
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        return files
                .stream()
                .map(this::uploadFile)
                .toList();
    }

    @Override
    public void deleteFile(String fileUrl) {
        String fileToDelete = extractFileNameFromUrl(fileUrl);
        s3Client.deleteObject(
                DeleteObjectRequest
                        .builder()
                        .bucket(BUCKET_NAME)
                        .key(fileToDelete)
                        .build()
        );
    }

    @Override
    public void deleteFiles(List<String> fileUrls) {
        fileUrls.forEach(this::deleteFile);
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}

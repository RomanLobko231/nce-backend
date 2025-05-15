package com.nce.backend.file_storage.infrastructure.aws;

import com.nce.backend.file_storage.domain.FileStorageService;
import com.nce.backend.common.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3FIleStorageService implements FileStorageService<MultipartFile> {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String BUCKET_NAME;

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

        return uniqueFileName;
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
    @Async
    public void deleteFiles(List<String> fileUrls) {
        fileUrls.forEach(this::deleteFile);
    }

    @Override
    public String generatePresignedUrl(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toExternalForm();
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}

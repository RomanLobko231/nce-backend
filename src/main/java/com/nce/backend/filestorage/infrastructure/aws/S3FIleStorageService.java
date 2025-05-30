package com.nce.backend.filestorage.infrastructure.aws;

import com.nce.backend.filestorage.domain.FileStorageService;
import com.nce.backend.common.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
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

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class S3FIleStorageService implements FileStorageService<MultipartFile> {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.private-bucket-name}")
    private String PRIVATE_BUCKET_NAME;

    @Value("${aws.s3.public-bucket-name}")
    private String PUBLIC_BUCKET_NAME;

    private static final Set<String> PUBLIC_IMAGE_TYPES = Set.of(
            "image/png",
            "image/jpeg"
    );

    private static final Set<String> PRIVATE_DOC_TYPES = Set.of(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/pdf"
    );

    @Override
    public String uploadFile(MultipartFile file) {
        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String bucket = this.getBucketByFileType(file);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
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

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(uniqueFileName)).toExternalForm();
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
        String fileToDelete = this.extractFileNameFromUrl(fileUrl);
        String bucket = this.extractBucketNameFromUrl(fileUrl);

        s3Client.deleteObject(
                DeleteObjectRequest
                        .builder()
                        .bucket(bucket)
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
    public String generatePresignedUrl(String fileUrl) {
        String key = this.extractFileNameFromUrl(fileUrl);

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(PRIVATE_BUCKET_NAME)
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
        String key = url.substring(url.lastIndexOf("/") + 1);

        return URLDecoder.decode(key, StandardCharsets.UTF_8);
    }

    private String extractBucketNameFromUrl(String url) {
        URI uri = URI.create(url);

        return uri
                .getHost()
                .split("\\.")[0];
    }

    private String getBucketByFileType(MultipartFile file) {
        String fileType = file.getContentType();

        if (fileType == null || fileType.isBlank()) {
            throw new FileProcessingException("File content type is null or empty");
        }

        if (PUBLIC_IMAGE_TYPES.contains(fileType)) {
            return PUBLIC_BUCKET_NAME;
        }

        if (PRIVATE_DOC_TYPES.contains(fileType)) {
            return PRIVATE_BUCKET_NAME;
        }

        throw new FileProcessingException("Unsupported file type: " + fileType);
    }
}

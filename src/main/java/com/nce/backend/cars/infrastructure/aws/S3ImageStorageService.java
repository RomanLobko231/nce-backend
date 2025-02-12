package com.nce.backend.cars.infrastructure.aws;

import com.nce.backend.cars.domain.services.ImageStorageService;
import com.nce.backend.cars.exceptions.ImageProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ImageStorageService implements ImageStorageService<MultipartFile> {

    private final S3Client s3Client;

    private final String BUCKET_NAME = "maro-nalo";

    @Override
    public String uploadImage(MultipartFile image) {
        String uniqueFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(uniqueFileName)
                .contentType(image.getContentType())
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(image.getInputStream(), image.getInputStream().available()));
        } catch (IOException e) {
            throw new ImageProcessingException(
                    "Image '%s' could not be processed".formatted(image.getOriginalFilename())
            );
        }

        return s3Client.utilities().getUrl(builder -> builder.bucket(BUCKET_NAME).key(uniqueFileName)).toExternalForm();
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        return images
                .stream()
                .map(this::uploadImage)
                .toList();
    }

    @Override
    public void deleteImage(String url) {
        String fileName = extractFileNameFromUrl(url);
        s3Client.deleteObject(
                DeleteObjectRequest
                        .builder()
                        .bucket(BUCKET_NAME)
                        .key(fileName)
                        .build()
        );
    }

    @Override
    public void deleteImages(List<String> imageUrls) {
        imageUrls.forEach(this::deleteImage);
    }

    private String extractFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}

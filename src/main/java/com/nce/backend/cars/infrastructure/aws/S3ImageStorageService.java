package com.nce.backend.cars.infrastructure.aws;

import com.nce.backend.cars.domain.services.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3ImageStorageService implements ImageStorageService {

    private final S3Client s3Client;

    @Override
    public String uploadImage(String fileName, InputStream inputStream, String contentType) {
        return "";
    }

    @Override
    public InputStream downloadImage(String fileName) {
        return null;
    }

    @Override
    public void deleteImage(String fileName) {

    }

}

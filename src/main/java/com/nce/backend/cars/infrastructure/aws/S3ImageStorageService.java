package com.nce.backend.cars.infrastructure.aws;

import com.nce.backend.cars.domain.services.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3ImageStorageService implements ImageStorageService {

    private final S3Client s3Client;

    private final String BUCKET_NAME = "maro-nalo";

    @Override
    public String uploadImage(String fileName, InputStream inputStream, String contentType) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, inputStream.available()));

        return s3Client.utilities().getUrl(builder -> builder.bucket(BUCKET_NAME).key(fileName)).toExternalForm();
    }

    @Override
    public InputStream downloadImage(String fileName) {
        return null;
    }

    @Override
    public void deleteImage(String fileName) {

    }

}

package com.nce.backend.file_storage.infrastructure.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AWSConfig {

    private final Region REGION = Region.EU_NORTH_1;

    @Value("${aws.s3.access-key-id}")
    private String accessKeyId;

    @Value("${aws.s3.secret-access-key}")
    private String secretAccessKey;

    @Bean
    S3Client s3Client() {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return  S3Client
                .builder()
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    S3Presigner s3Presigner() {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return S3Presigner
                .builder()
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}

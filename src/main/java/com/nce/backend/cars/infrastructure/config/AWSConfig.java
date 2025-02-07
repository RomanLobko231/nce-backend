package com.nce.backend.cars.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {

    private final String BUCKET_NAME = "test-bucket";
    private final Region REGION = Region.EU_NORTH_1;

    @Value("${AWS_ACCESS_KEY_ID}")
    private String accessKeyId;

    @Value("${AWS_SECRET_ACCESS_KEY}")
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
}

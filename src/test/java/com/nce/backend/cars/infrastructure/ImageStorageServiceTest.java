package com.nce.backend.cars.infrastructure;

import com.nce.backend.cars.infrastructure.aws.S3ImageStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ImageStorageServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3ImageStorageService s3ImageStorageService;

    @Test
    void uploadImage_ShouldReturnUrl_WhenUploadIsSuccessful() throws IOException {

        String fileName = "test-image.jpg";
        String contentType = "image/jpeg";
        InputStream inputStream = new ByteArrayInputStream("fake image content".getBytes());

        PutObjectRequest expectedRequest = PutObjectRequest.builder()
                .bucket("maro-nalo")
                .key(fileName)
                .contentType(contentType)
                .build();

        S3Utilities s3Utilities = Mockito.mock(S3Utilities.class);
        Mockito.when(s3Client.utilities()).thenReturn(s3Utilities);
        Mockito.when(s3Utilities.getUrl(Mockito.any(Consumer.class)))
                .thenReturn(new URL("https://maro-nalo.s3.eu-north-1.amazonaws.com/test-image.jpg"));

        String resultUrl = s3ImageStorageService.uploadImage(fileName, inputStream, contentType);


        Mockito.verify(s3Client).putObject(Mockito.eq(expectedRequest), Mockito.any(RequestBody.class));
        assertEquals(resultUrl, "https://maro-nalo.s3.eu-north-1.amazonaws.com/" + fileName);
    }

}

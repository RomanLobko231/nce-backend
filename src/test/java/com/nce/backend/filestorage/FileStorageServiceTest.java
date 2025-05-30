package com.nce.backend.filestorage;

import com.nce.backend.filestorage.infrastructure.aws.S3FIleStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileStorageServiceTest {
    @Mock
    private S3Client s3Client;

    @InjectMocks
    private S3FIleStorageService fileStorageService;

    @Test
    void uploadImage_ShouldReturnUrl_WhenUploadIsSuccessful() throws IOException {

        MultipartFile mockMultipartFile = mock(MultipartFile.class);
        InputStream mockInputStream = new ByteArrayInputStream("test image content".getBytes());
        String expectedUrl = "https://maro-nalo.s3.eu-north-1.amazonaws.com/test-image.png";

        when(mockMultipartFile.getInputStream()).thenReturn(mockInputStream);
        when(mockMultipartFile.getOriginalFilename()).thenReturn("test-image.png");
        when(mockMultipartFile.getContentType()).thenReturn("image/png");

        PutObjectRequest expectedRequest = PutObjectRequest.builder()
                .bucket("maro-nalo")
                .key("test-image.png")
                .contentType("image/png")
                .build();

        S3Utilities s3Utilities = Mockito.mock(S3Utilities.class);
        Mockito.when(s3Client.utilities()).thenReturn(s3Utilities);
        Mockito.when(s3Utilities.getUrl(Mockito.any(Consumer.class)))
                .thenReturn(new URL("https://maro-nalo.s3.eu-north-1.amazonaws.com/test-image.png"));

        String resultUrl = fileStorageService.uploadFile(mockMultipartFile);

        assertEquals(expectedUrl, resultUrl);
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        verify(s3Utilities, times(1)).getUrl(Mockito.any(Consumer.class));
    }

    @Test
    void testDeleteImage() {
        String imageUrl = "https://maro-nalo.s3.eu-north-1.amazonaws.com/1739318195332_car8.jpg";
        String expectedFileName = "1739318195332_car8.jpg";

        fileStorageService.deleteFile(imageUrl);

        verify(s3Client).deleteObject(DeleteObjectRequest.builder()
                .bucket("maro-nalo")
                .key(expectedFileName)
                .build());

        verifyNoMoreInteractions(s3Client);
    }

    @Test
    void testDeleteImages() {
        List<String> imageUrls = List.of(
                "https://maro-nalo.s3.eu-north-1.amazonaws.com/1739318195332_car8.jpg",
                "https://maro-nalo.s3.eu-north-1.amazonaws.com/1739318195333_car9.jpg"
        );

        fileStorageService.deleteFiles(imageUrls);

        verify(s3Client).deleteObject(DeleteObjectRequest.builder()
                .bucket("maro-nalo")
                .key("1739318195332_car8.jpg")
                .build());

        verify(s3Client).deleteObject(DeleteObjectRequest.builder()
                .bucket("maro-nalo")
                .key("1739318195333_car9.jpg")
                .build());

        verifyNoMoreInteractions(s3Client);
    }
}

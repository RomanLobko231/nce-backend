package com.nce.backend.cars.domain.services;

import java.io.IOException;
import java.io.InputStream;

public interface ImageStorageService {
    String uploadImage(String fileName, InputStream inputStream, String contentType) throws IOException;
    InputStream downloadImage(String fileName);
    void deleteImage(String fileName);
}

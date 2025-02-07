package com.nce.backend.cars.domain.services;

import java.io.InputStream;

public interface ImageStorageService {
    String uploadImage(String fileName, InputStream inputStream, String contentType);
    InputStream downloadImage(String fileName);
    void deleteImage(String fileName);
}

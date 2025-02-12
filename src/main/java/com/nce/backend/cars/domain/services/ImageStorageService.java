package com.nce.backend.cars.domain.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ImageStorageService<T> {
    String uploadImage(T image);
    List<String> uploadImages(List<T> images);
    void deleteImage(String fileName);
    void deleteImages(List<String> fileNames);
}

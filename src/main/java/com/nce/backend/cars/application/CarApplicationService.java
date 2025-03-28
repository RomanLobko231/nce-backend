package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.common.events.NewCarSavedEvent;
import com.nce.backend.cars.domain.services.CarDomainService;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.file_storage.FileStorageFacade;
import com.nce.backend.file_storage.domain.FileStorageService;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarApplicationService {

    private final CarDomainService carDomainService;
    private final ExternalApiService externalApiService;
    private final FileStorageFacade fileStorageFacade;

    public Car addCarAsCustomer(Car carToAdd, List<MultipartFile> imagesToUpload) {
        if (imagesToUpload != null && !imagesToUpload.isEmpty()) {
            List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
            carToAdd.setImagePaths(imageUrls);
        }

        return carDomainService.saveNewCarRequest(carToAdd);
    }

    public Car addCarAsAdmin(Car carToAdd, List<MultipartFile> imagesToUpload) {
        if (imagesToUpload != null && !imagesToUpload.isEmpty()) {
            List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
            carToAdd.addNewImagePaths(imageUrls);
        }

        return carDomainService.save(carToAdd);
    }

    public Car updateCar(Car car, List<MultipartFile> imagesToUpload) {
        Car existingCar = carDomainService
                .findById(car.getId())
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(car.getId()))
                );

        List<String> existingImagePaths = existingCar.getImagePaths();
        List<String> updatedImagePaths = car.getImagePaths();

        List<String> imagesToDelete = existingImagePaths
                .stream()
                .filter(image -> !updatedImagePaths.contains(image))
                .toList();

        if (!imagesToDelete.isEmpty()) {
            fileStorageFacade.deleteFiles(imagesToDelete);
        }

        if (imagesToUpload != null && !imagesToUpload.isEmpty()) {
            List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
            car.addNewImagePaths(imageUrls);
        }

        return carDomainService.save(car);
    }

    public void addImagesForCarById(UUID carId, List<MultipartFile> imagesToUpload) {
        if (imagesToUpload == null || imagesToUpload.isEmpty()) return;

        Car car = carDomainService
                .findById(carId)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(carId))
                );

        List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
        car.addNewImagePaths(imageUrls);

        carDomainService.save(car);
    }

    public List<Car> getAllCars() {
        return carDomainService.getAll();
    }

    public void deleteById(UUID id) {
        carDomainService.deleteById(id);
    }

    public Car findById(UUID id) {
        return carDomainService
                .findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(id))
                );
    }

    @Async
    @TransactionalEventListener
    public void saveApiDataOn(NewCarSavedEvent event) {
        Car carToUpdate = carDomainService
                .findById(event.carId())
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(event.carId()))
                );
        ApiCarData apiData = externalApiService.fetchCarData(event.registrationNumber());
        carToUpdate.updateDataFromApi(apiData);

        carDomainService.save(carToUpdate);
    }


}

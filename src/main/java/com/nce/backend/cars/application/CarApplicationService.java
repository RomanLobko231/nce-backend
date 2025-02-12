package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.events.NewCarSavedEvent;
import com.nce.backend.cars.domain.services.CarDomainService;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.cars.domain.services.ImageStorageService;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.cars.exceptions.ImageProcessingException;
import com.nce.backend.cars.ui.requests.AddCarAdminRequest;
import com.nce.backend.cars.ui.requests.AddCarCustomerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarApplicationService {

    private final CarDomainService carDomainService;
    private final ExternalApiService externalApiService;
    private final ImageStorageService<MultipartFile> imageStorageService;

    public Car addCarAsCustomer(Car carToAdd) {
        return carDomainService.saveNewCarRequest(carToAdd);
    }

    public Car addCarAsAdmin(Car carToAdd, List<MultipartFile> images) {
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = imageStorageService.uploadImages(images);
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
            imageStorageService.deleteImages(imagesToDelete);
        }

        if (imagesToUpload != null && !imagesToUpload.isEmpty()) {
            List<String> imageUrls = imageStorageService.uploadImages(imagesToUpload);
            car.addNewImagePaths(imageUrls);
        }

        return carDomainService.save(car);
    }

    public void addImagesForCarById(UUID carId, List<MultipartFile> images) {
        if (images == null) return;

        Car car = carDomainService
                .findById(carId)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(carId))
                );

        List<String> imageUrls = imageStorageService.uploadImages(images);
        car.addNewImagePaths(imageUrls);

        carDomainService.save(car);
    }

    public List<Car> getAllCars() {
        return carDomainService.getAll();
    }

    public void deleteById(UUID id) {
        if (!carDomainService.existsById(id))
            throw new NoSuchElementException("Car with id %s was not found".formatted(id));
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
        ApiCarData apiData = externalApiService.fetchCarData(event.registrationNumber());
        Car carToUpdate = carDomainService
                .findById(event.id())
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(event.id()))
                );

        carToUpdate.setMake(apiData.make());
        carToUpdate.setModel(apiData.model());
        carToUpdate.setColor(apiData.color());
        carToUpdate.setEngineType(apiData.engineType());
        carToUpdate.setEngineVolume(apiData.engineVolume());
        carToUpdate.setFirstTimeRegisteredInNorway(apiData.firstTimeRegisteredInNorway());
        carToUpdate.setNextEUControl(apiData.nextEUControl());
        carToUpdate.setGearboxType(apiData.gearboxType());
        carToUpdate.setNumberOfDoors(apiData.numberOfDoors());
        carToUpdate.setNumberOfSeats(apiData.numberOfSeats());
        carToUpdate.setBodywork(apiData.bodywork());
        carToUpdate.setOperatingMode(apiData.operatingMode());
        carToUpdate.setWeight(apiData.weight());

        carDomainService.save(carToUpdate);
    }


}

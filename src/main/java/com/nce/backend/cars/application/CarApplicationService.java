package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.cars.exceptions.CarAlreadyExistsException;
import com.nce.backend.cars.exceptions.OnAuctionUpdateNotAllowedException;
import com.nce.backend.common.events.*;
import com.nce.backend.cars.domain.services.CarDomainService;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.file_storage.FileStorageFacade;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarApplicationService {

    private final CarDomainService carDomainService;
    private final ExternalApiService externalApiService;
    private final FileStorageFacade fileStorageFacade;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public Car validateSaveCar(Car car, List<MultipartFile> imagesToUpload) {
        if (carDomainService.existsByRegNumber(car.getRegistrationNumber())) {
            throw new CarAlreadyExistsException(
                    "Car with the registration number '%s' already exists".formatted(car.getRegistrationNumber())
            );
        }

        this.addImagesForCar(car, imagesToUpload);
        car.setStatus(Status.IN_REVIEW);

        Car savedCar = carDomainService.save(car);
        eventPublisher.publishEvent(
                new NewCarSavedEvent(savedCar.getId(), savedCar.getOwnerID())
        );

        return savedCar;
    }

    @Transactional
    public Car addCarSimplified(Car carToAdd, List<MultipartFile> imagesToUpload) {
        Car savedCar = validateSaveCar(carToAdd, imagesToUpload);
        this.addApiDataForCar(savedCar);

        return savedCar;
    }

    @Transactional
    public Car addCarComplete(Car carToAdd, List<MultipartFile> imagesToUpload) {
        return validateSaveCar(carToAdd, imagesToUpload);
    }


    @Transactional
    public Car updateCar(Car carToUpdate, List<MultipartFile> imagesToUpload) {
        Car existingCar = carDomainService
                .findById(carToUpdate.getId())
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(carToUpdate.getId()))
                );

        if(existingCar.getStatus() == Status.ON_AUCTION){
            throw new OnAuctionUpdateNotAllowedException("Car is already on the auction. Cannot update.");
        }

        this.compareAndDeleteImages(
                existingCar.getImagePaths(), carToUpdate.getImagePaths()
        );
        this.addImagesForCar(carToUpdate, imagesToUpload);
        carToUpdate.setStatus(existingCar.getStatus());

        return carDomainService.update(carToUpdate);
    }

    public void addImagesForCarById(UUID carId, List<MultipartFile> imagesToUpload) {
        Car car = carDomainService
                .findById(carId)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(carId))
                );

        this.addImagesForCar(car, imagesToUpload);

        carDomainService.save(car);
    }

    public List<Car> getAllCars() {
        return carDomainService.getAll();
    }

    public List<Car> getAllCarsByStatus(Status status) {
        return carDomainService.getAllCarsByStatus(status);
    }

    public List<Car> getAllCarsByOwnerId(UUID ownerId) {
        return carDomainService.getAllCarsByOwnerId(ownerId);
    }

    @Transactional
    public void deleteById(UUID id) {
        Car carToDelete = carDomainService
                .findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(id))
                );

        carDomainService.deleteById(id);
        eventPublisher.publishEvent(new CarDeletedEvent(id));

        List<String> imagesUrls = carToDelete.getImagePaths();
        fileStorageFacade.deleteFiles(imagesUrls);

    }

    public Car findById(UUID id) {
        return carDomainService
                .findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(id))
                );
    }

    private void compareAndDeleteImages(List<String> oldImageUrls, List<String> newImageUrls) {
        List<String> imagesToDelete = oldImageUrls
                .stream()
                .filter(image -> !newImageUrls.contains(image))
                .toList();

        fileStorageFacade.deleteFiles(imagesToDelete);
    }

    private void addImagesForCar(Car car, List<MultipartFile> imagesToUpload) {
        List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
        car.addNewImagePaths(imageUrls);
    }

    private void addApiDataForCar(Car car) {
        externalApiService
                .fetchCarData(car.getRegistrationNumber())
                .thenAccept(data -> {
                    car.updateDataFromApi(data);
                    carDomainService.save(car);
                })
                .exceptionally(ex -> {
                    log.warn("Failed to fetch car data for {}: {}", car.getRegistrationNumber(), ex.getMessage());
                    return null;
                });
    }

    public void updateCarStatus(Status status, UUID carId) {
        if (!carDomainService.existsById(carId)) {
            throw new NoSuchElementException(
                    "Car with id '%s' does not exist".formatted(carId)
            );
        }

        carDomainService.updateCarStatus(status, carId);
    }

    public boolean existsByRegNumber(String regNumber) {
        return carDomainService.existsByRegNumber(regNumber);
    }

    @Async("eventTaskExecutor")
    @TransactionalEventListener
    public void cleanDatabaseRelationsOn(UserDeletedEvent event) {
        carDomainService.deleteByOwnerId(event.id());
    }

    @Async("eventTaskExecutor")
    @TransactionalEventListener
    public void updateCarOn(NewAuctionStarted event) {
        carDomainService.updateCarStatus(Status.ON_AUCTION, event.carId());
    }

}

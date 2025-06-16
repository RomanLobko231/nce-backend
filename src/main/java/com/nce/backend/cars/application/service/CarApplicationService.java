package com.nce.backend.cars.application.service;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.PaginatedResult;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.cars.exceptions.CarAlreadyExistsException;
import com.nce.backend.cars.exceptions.UpdateNotAllowedException;
import com.nce.backend.cars.domain.services.CarDomainService;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.common.annotation.logging.LoggableAction;
import com.nce.backend.common.event.auction.AuctionEndedEvent;
import com.nce.backend.common.event.auction.AuctionRestartedEvent;
import com.nce.backend.common.event.auction.NewAuctionStartedEvent;
import com.nce.backend.common.event.car.CarDeletedEvent;
import com.nce.backend.common.event.car.NewCarSavedEvent;
import com.nce.backend.common.event.user.UserDeletedEvent;
import com.nce.backend.filestorage.FileStorageFacade;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

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
    @LoggableAction(action = "ADD_CAR")
    public Car addCarSimplified(Car carToAdd, List<MultipartFile> imagesToUpload) {
        Car savedCar = validateSaveCar(carToAdd, imagesToUpload);
        this.addApiDataForCar(savedCar);

        return savedCar;
    }

    @Transactional
    @LoggableAction(action = "ADD_CAR")
    public Car addCarComplete(Car carToAdd, List<MultipartFile> imagesToUpload) {
        return validateSaveCar(carToAdd, imagesToUpload);
    }


    @Transactional
    @LoggableAction(action = "UPDATE_CAR", affectedIdParam = "#carToUpdate.id")
    public Car updateCar(Car carToUpdate, List<MultipartFile> imagesToUpload) {
        Car existingCar = carDomainService
                .findById(carToUpdate.getId())
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(carToUpdate.getId()))
                );

        if (existingCar.getStatus() == Status.ON_AUCTION) {
            throw new UpdateNotAllowedException("Car is already on the auction. Cannot update.");
        }

        if (existingCar.getStatus() == Status.SOLD) {
            throw new UpdateNotAllowedException("Car is already sold. Cannot update.");
        }

        this.compareAndDeleteImages(
                existingCar.getImagePaths(), carToUpdate.getImagePaths()
        );
        this.addImagesForCar(carToUpdate, imagesToUpload);
        carToUpdate.setStatus(existingCar.getStatus());

        return carDomainService.update(carToUpdate);
    }

    @LoggableAction(action = "UPDATE_CAR", affectedIdParam = "#carId")
    public void addImagesForCarById(UUID carId, List<MultipartFile> imagesToUpload) {
        Car car = carDomainService
                .findById(carId)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(carId))
                );

        this.addImagesForCar(car, imagesToUpload);

        carDomainService.save(car);
    }

    @LoggableAction(action = "GET_CAR_INFO")
    public PaginatedResult<Car> getAllCars(int page, int size) {
        return carDomainService.getAll(page, size);
    }

    @LoggableAction(action = "GET_CAR_INFO")
    public PaginatedResult<Car> getAllCarsByStatus(Status status, int page, int size) {
        return carDomainService.getAllCarsByStatus(status, page, size);
    }

    @LoggableAction(action = "GET_CAR_INFO", affectedIdParam = "#ownerId")
    public PaginatedResult<Car> getAllCarsByOwnerId(UUID ownerId, int page, int size) {
        return carDomainService.getAllCarsByOwnerId(ownerId, page, size);
    }

    @LoggableAction(action = "GET_CAR_INFO", affectedIdParam = "#ownerId")
    public PaginatedResult<Car> getAllCarsByOwnerAndStatus(Status status, UUID ownerId, int page, int size) {
        return carDomainService.getAllCarsByOwnerAndStatus(status, ownerId, page, size);
    }

    @Transactional
    @LoggableAction(action = "DELETE_CAR", affectedIdParam = "#id")
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

    @LoggableAction(action = "GET_CAR_INFO", affectedIdParam = "#id")
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

    @LoggableAction(action = "UPDATE_CAR", affectedIdParam = "#carId")
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
    public void setCarAuctionedOn(NewAuctionStartedEvent event) {
        carDomainService.updateCarStatus(Status.ON_AUCTION, event.carId());
    }

    @Async("eventTaskExecutor")
    @TransactionalEventListener
    public void setCarAuctionedOn(AuctionRestartedEvent event) {
        carDomainService.updateCarStatus(Status.ON_AUCTION, event.carId());
    }

    @Async("eventTaskExecutor")
    @EventListener
    public void setCarSoldOn(AuctionEndedEvent event) {
        carDomainService.updateCarStatus(Status.SOLD, event.carId());
    }
}

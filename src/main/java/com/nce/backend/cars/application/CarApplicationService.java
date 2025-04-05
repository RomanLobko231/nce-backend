package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.exceptions.CarAlreadyExistsException;
import com.nce.backend.common.events.CarSaveFailedRollbackUserEvent;
import com.nce.backend.common.events.NewCarSavedEvent;
import com.nce.backend.cars.domain.services.CarDomainService;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.file_storage.FileStorageFacade;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public Car addCarSimplified(Car carToAdd, List<MultipartFile> imagesToUpload) {
        if (carDomainService.existsByRegNumber(carToAdd.getRegistrationNumber())) {
            eventPublisher.publishEvent(new CarSaveFailedRollbackUserEvent(carToAdd.getOwnerID()));
            throw new CarAlreadyExistsException(
                    "Car with the registration number '%s' already exists".formatted(carToAdd.getRegistrationNumber())
            );
        }

        List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
        carToAdd.addNewImagePaths(imageUrls);

        return carDomainService.saveNewCarRequest(carToAdd);
    }

    @Transactional
    public Car addCarComplete(Car carToAdd, List<MultipartFile> imagesToUpload) {
        if (carDomainService.existsByRegNumber(carToAdd.getRegistrationNumber())) {
            eventPublisher.publishEvent(new CarSaveFailedRollbackUserEvent(carToAdd.getOwnerID()));
            throw new CarAlreadyExistsException(
                    "Car with the registration number '%s' already exists".formatted(carToAdd.getRegistrationNumber())
            );
        }

        List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
        carToAdd.addNewImagePaths(imageUrls);

        return carDomainService.save(carToAdd);
    }

    public Car updateCar(Car carToUpdate, List<MultipartFile> imagesToUpload) {
        Car existingCar = carDomainService
                .findById(carToUpdate.getId())
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(carToUpdate.getId()))
                );

        this.compareAndDeleteImages(
                existingCar.getImagePaths(), carToUpdate.getImagePaths()
        );

        List<String> imageUrls = fileStorageFacade.uploadFiles(imagesToUpload);
        carToUpdate.addNewImagePaths(imageUrls);

        return carDomainService.update(carToUpdate);
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
        Car carToDelete = carDomainService
                .findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Car with id %s was not found".formatted(id))
                );

        carDomainService.deleteById(id);

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

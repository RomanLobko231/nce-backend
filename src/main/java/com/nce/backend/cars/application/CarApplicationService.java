package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.events.NewCarSavedEvent;
import com.nce.backend.cars.domain.services.CarDomainService;
import com.nce.backend.cars.domain.services.ExternalApiService;
import com.nce.backend.cars.domain.valueObjects.ApiCarData;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.cars.ui.requests.AddCarRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarApplicationService {

    private final CarDomainService carDomainService;
    private final ExternalApiService externalApiService;

    public Car addNewCarRequest(AddCarRequest addCarRequest) {
        return carDomainService.saveNewCarRequest(
                Car.builder()
                        .registrationNumber(addCarRequest.carRegistrationNumber())
                        .ownerInfo(
                                OwnerInfo.builder()
                                        .name(addCarRequest.ownerName())
                                        .phoneNumber(addCarRequest.phoneNumber())
                                        .email(addCarRequest.email())
                                        .build()
                        )
                        .kilometers(addCarRequest.kilometers())
                        .status(Status.IN_REVIEW)
                        .build());
    }

    public List<Car> getAllCars() {
        return carDomainService.getAll();
    }

    public void deleteById(UUID id) {
        if (!carDomainService.existsById(id)) throw new NoSuchElementException("Car with id %s was not found".formatted(id));
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

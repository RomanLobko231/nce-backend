package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.services.CarDomainService;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.infrastructure.jpa.CarRepositoryImpl;
import com.nce.backend.cars.ui.requests.AddCarRequest;
import com.nce.backend.cars.ui.responses.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarApplicationService {

    private final CarDomainService carDomainService;

    public Car addNewCar(AddCarRequest addCarRequest) {
        return carDomainService.saveCar(
                Car.builder()
                        .registrationNumber(addCarRequest.carRegistrationNumber())
                        .ownerInfo(
                                OwnerInfo.builder()
                                        .name(addCarRequest.ownerName())
                                        .phoneNumber(addCarRequest.phoneNumber())
                                        .email(addCarRequest.email())
                                        .build()
                        )
                        .build());
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
}

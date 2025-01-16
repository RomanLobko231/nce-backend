package com.nce.backend.cars.domain.services;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.events.NewCarSavedEvent;
import com.nce.backend.cars.domain.repositories.CarRepository;
import com.nce.backend.cars.exceptions.CarAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarDomainService {

    private final CarRepository carRepository;

    private final ApplicationEventPublisher eventPublisher;

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public void deleteById(UUID id) {
        carRepository.deleteById(id);
    }

    public Optional<Car> findById(UUID id) {
        return carRepository.findById(id);
    }

    @Transactional
    public Car saveNewCarRequest(Car car) {
        if (carRepository.existsByRegNumber(car.getRegistrationNumber())) {
            throw new CarAlreadyExistsException(
                    "Car with the registration number '%s' already exists".formatted(car.getRegistrationNumber())
            );
        }

        Car savedCar = carRepository.save(car);
        eventPublisher.publishEvent(
                new NewCarSavedEvent(savedCar.getId(), savedCar.getRegistrationNumber())
        );

        return savedCar;
    }
}

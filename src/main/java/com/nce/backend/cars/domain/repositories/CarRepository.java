package com.nce.backend.cars.domain.repositories;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    Optional<Car> findById(UUID id);
    List<Car> findAll();
    List<Car> findAllByOwnerId(UUID id);
    Car save(Car car);
    void deleteById(UUID id);
    boolean existsByRegNumber(String registrationNumber);
    boolean existsById(UUID id);
    List<Car> findAllByStatus(Status status);
    void updateCarStatusById(Status status, UUID carId);
    void deleteByOwnerId(UUID id);
}

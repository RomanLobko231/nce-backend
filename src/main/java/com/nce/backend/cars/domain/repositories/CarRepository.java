package com.nce.backend.cars.domain.repositories;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.PaginatedResult;
import com.nce.backend.cars.domain.valueObjects.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    Optional<Car> findById(UUID id);
    List<Car> findAll();
    PaginatedResult<Car> findAllByOwnerId(UUID id, int page, int size);
    Car save(Car car);
    void deleteById(UUID id);
    boolean existsByRegNumber(String registrationNumber);
    boolean existsById(UUID id);
    PaginatedResult<Car> findAllByStatus(Status status, int page, int size);
    void updateCarStatusById(Status status, UUID carId);
    void deleteByOwnerId(UUID id);
}

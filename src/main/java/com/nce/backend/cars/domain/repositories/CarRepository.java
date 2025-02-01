package com.nce.backend.cars.domain.repositories;

import com.nce.backend.cars.domain.entities.Car;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {
    Optional<Car> findById(UUID id);
    List<Car> findAll();
    Car save(Car car);
    void deleteById(UUID id);
    boolean existsByRegNumber(String registrationNumber);
    boolean existsById(UUID id);

}

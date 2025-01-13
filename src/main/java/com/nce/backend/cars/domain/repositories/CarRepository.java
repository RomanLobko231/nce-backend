package com.nce.backend.cars.domain.repositories;

import com.nce.backend.cars.domain.entities.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Optional<Car> findById(long id);
    List<Car> findAll();
    Car save(Car car);
    void delete(Car car);
}

package com.nce.backend.cars.domain.services;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.PaginatedResult;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.cars.domain.repositories.CarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarDomainService {

    private final CarRepository carRepository;

    @Transactional
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Transactional
    public Car update(Car car) {
        return carRepository.save(car);
    }

    public PaginatedResult<Car> getAll(int page, int size) {
        return carRepository.findAll(page, size);
    }

    @Transactional
    public void deleteById(UUID id) {
        carRepository.deleteById(id);
    }

    public Optional<Car> findById(UUID id) {
        return carRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return carRepository.existsById(id);
    }

    public boolean existsByRegNumber(String regNumber) {
        return carRepository.existsByRegNumber(regNumber);
    }

    public PaginatedResult<Car> getAllCarsByOwnerId(UUID ownerId, int page, int size) {
        return carRepository.findAllByOwnerId(ownerId, page, size);
    }

    public PaginatedResult<Car> getAllCarsByStatus(Status status, int page, int size) {
        return carRepository.findAllByStatus(status, page, size);
    }

    public PaginatedResult<Car> getAllCarsByOwnerAndStatus(Status status, UUID ownerId, int page, int size) {
        return carRepository.findAllByOwnerAndStatus(status, ownerId, page, size);
    }

    @Transactional
    public void updateCarStatus(Status status, UUID carId) {
        carRepository.updateCarStatusById(status, carId);
    }

    public void deleteByOwnerId(UUID id) {
        carRepository.deleteByOwnerId(id);
    }

}

package com.nce.backend.cars.domain.services;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.common.events.CarDeletedEvent;
import com.nce.backend.common.events.NewCarSavedEvent;
import com.nce.backend.cars.domain.repositories.CarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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

    public List<Car> getAll() {
        return carRepository.findAll();
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

    public List<Car> getAllCarsByOwnerId(UUID ownerId) {
        return carRepository.findAllByOwnerId(ownerId);
    }

    public List<Car> getAllCarsByStatus(Status status) {
        return carRepository.findAllByStatus(status);
    }

    @Transactional
    public void updateCarStatus(Status status, UUID carId) {
        carRepository.updateCarStatusById(status, carId);
    }

    public void deleteByOwnerId(UUID id) {
        carRepository.deleteByOwnerId(id);
    }
}

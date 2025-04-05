package com.nce.backend.cars.domain.services;

import com.nce.backend.cars.domain.entities.Car;
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
    private final ApplicationEventPublisher eventPublisher;

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
        eventPublisher.publishEvent(new CarDeletedEvent(id));
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

    @Transactional
    public Car saveNewCarRequest(Car car) {
        Car savedCar = carRepository.save(car);
        eventPublisher.publishEvent(
                new NewCarSavedEvent(savedCar.getId(), savedCar.getOwnerID(), savedCar.getRegistrationNumber())
        );

        return savedCar;
    }
}

package com.nce.backend.cars.domain.services;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.repositories.CarRepository;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarDomainService {

    private final CarRepository carRepository;

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }
}

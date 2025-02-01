package com.nce.backend.cars.infrastructure.jpa;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
@RequiredArgsConstructor
public class CarRepositoryImpl implements CarRepository {

    private final CarJpaRepository carJpaRepository;
    private final CarJpaEntityMapper mapper;

    @Override
    public Optional<Car> findById(UUID id) {
        return carJpaRepository
                .findById(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Car> findAll() {
        return carJpaRepository
                .findAll()
                .stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Car save(Car car) {
        CarJpaEntity entity = mapper.toJpaEntity(car);
        CarJpaEntity savedEntity = carJpaRepository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public void deleteById(UUID id) {
        carJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByRegNumber(String registrationNumber) {
        return carJpaRepository.existsByRegistrationNumber(registrationNumber);
    }

    @Override
    public boolean existsById(UUID id) {
        return carJpaRepository.existsById(id);
    }
}

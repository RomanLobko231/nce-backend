package com.nce.backend.cars.infrastructure.jpa;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.repositories.CarRepository;
import com.nce.backend.cars.domain.valueObjects.PaginatedResult;
import com.nce.backend.cars.domain.valueObjects.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PaginatedResult<Car> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarJpaEntity> result = carJpaRepository.findAll(pageable);

        return mapper.toPaginatedResult(result);
    }

    @Override
    public PaginatedResult<Car> findAllByOwnerId(UUID ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarJpaEntity> result = carJpaRepository.findAllByOwnerId(ownerId, pageable);

        return mapper.toPaginatedResult(result);
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

    @Override
    public PaginatedResult<Car> findAllByStatus(Status status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarJpaEntity> result = carJpaRepository.findAllByStatus(status.name(), pageable);

        return mapper.toPaginatedResult(result);
    }

    @Override
    public void updateCarStatusById(Status status, UUID carId) {
        carJpaRepository.updateCarStatusById(status.name(), carId);
    }

    @Override
    public void deleteByOwnerId(UUID id) {
        carJpaRepository.deleteByOwnerId(id);
    }

    @Override
    public PaginatedResult<Car> findAllByOwnerAndStatus(Status status, UUID ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CarJpaEntity> result = carJpaRepository.findAllByOwnerAndStatus(status.name(), ownerId, pageable);

        return mapper.toPaginatedResult(result);
    }
}

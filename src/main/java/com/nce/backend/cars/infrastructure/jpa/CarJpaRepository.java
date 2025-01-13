package com.nce.backend.cars.infrastructure.jpa;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.repositories.CarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarJpaRepository extends CarRepository, JpaRepository<Car, UUID> {

}

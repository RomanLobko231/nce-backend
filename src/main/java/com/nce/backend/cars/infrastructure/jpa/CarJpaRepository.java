package com.nce.backend.cars.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID> {
}

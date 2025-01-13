package com.nce.backend.cars.infrastructure.jpa;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity(name = "car")
@Table(name = "car")
public class CarJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}

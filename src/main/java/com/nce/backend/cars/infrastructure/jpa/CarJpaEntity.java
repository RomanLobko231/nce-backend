package com.nce.backend.cars.infrastructure.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "car")
@Table(name = "car")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CarJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}

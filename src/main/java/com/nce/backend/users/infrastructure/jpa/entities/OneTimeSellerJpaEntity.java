package com.nce.backend.users.infrastructure.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity(name = "one_time_seller")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "one_time_seller_id")
public class OneTimeSellerJpaEntity extends UserJpaEntity {

    @Builder.Default
    private UUID carId = UUID.fromString("00000000-0000-0000-0000-000000000000");
}

package com.nce.backend.cars.infrastructure.jpa;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OwnerInfoJpaEntity {

    @NotNull(message = "Name can't be null")
    String name;

    @NotNull(message = "Phone number can't be null")
    String phoneNumber;

    @Nullable
    String email;
}

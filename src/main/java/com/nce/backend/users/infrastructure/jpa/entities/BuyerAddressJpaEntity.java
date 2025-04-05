package com.nce.backend.users.infrastructure.jpa.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class BuyerAddressJpaEntity {

    @NotNull(message = "Country cannot be null")
    @NotBlank(message = "Country cannot be blank")
    String country;

    @NotNull(message = "Street address cannot be null")
    @NotBlank(message = "Street address cannot be blank")
    String streetAddress;

    @NotNull(message = "Postal location cannot be null")
    @NotBlank(message = "Postal location cannot be blank")
    String postalLocation;

    @NotNull(message = "Postal code cannot be null")
    String postalCode;
}

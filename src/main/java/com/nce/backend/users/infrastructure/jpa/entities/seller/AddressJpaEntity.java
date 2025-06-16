package com.nce.backend.users.infrastructure.jpa.entities.seller;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class AddressJpaEntity {

    @NotNull(message = "Street address cannot be null")
    @NotBlank(message = "Street address cannot be blank")
    @Size(min = 20, message = "min 20")
    String streetAddress;

    @NotNull(message = "Postal location cannot be null")
    @NotBlank(message = "Postal location cannot be blank")
    String postalLocation;

    @NotNull(message = "Postal code cannot be null")
    @NotBlank(message = "Postal code cannot be blank")
    String postalCode;
}

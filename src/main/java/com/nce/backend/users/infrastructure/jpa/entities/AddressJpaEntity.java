package com.nce.backend.users.infrastructure.jpa.entities;

import com.nce.backend.users.domain.valueObjects.Address;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    String streetAddress;

    @NotNull(message = "Postal location cannot be null")
    @NotBlank(message = "Postal location cannot be blank")
    String postalLocation;

    @NotNull(message = "Postal code cannot be null")
    String postalCode;

    public Address toDomainEntity(){
        return Address
                .builder()
                .streetAddress(streetAddress)
                .postalLocation(postalLocation)
                .postalCode(postalCode)
                .build();
    }
}

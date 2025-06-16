package com.nce.backend.users.domain.valueObjects;

import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String streetAddress;
    private String postalLocation;
    private String postalCode;
}

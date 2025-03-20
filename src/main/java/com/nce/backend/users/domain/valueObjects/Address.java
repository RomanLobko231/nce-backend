package com.nce.backend.users.domain.valueObjects;

import lombok.Builder;

@Builder
public record Address(String streetAddress, String postalLocation, String postalCode) {

}

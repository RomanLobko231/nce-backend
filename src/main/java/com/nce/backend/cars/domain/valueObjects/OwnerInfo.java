package com.nce.backend.cars.domain.valueObjects;

import lombok.Builder;

@Builder
public record OwnerInfo(String name, String phoneNumber, String email) {

}

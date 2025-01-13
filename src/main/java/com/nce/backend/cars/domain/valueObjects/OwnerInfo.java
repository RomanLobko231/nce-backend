package com.nce.backend.cars.domain.valueObjects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public record OwnerInfo(String name, String phoneNumber, String email) {

}

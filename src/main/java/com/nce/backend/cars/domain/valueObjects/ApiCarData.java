package com.nce.backend.cars.domain.valueObjects;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ApiCarData(
         String make,

         String model,

         LocalDate firstTimeRegisteredInNorway,

         String engineType,

         Double engineVolume,

         String bodywork,

         Integer numberOfSeats,

         Integer numberOfDoors,

         String color,

         GearboxType gearboxType,

         OperatingMode operatingMode,

         Integer weight,

         LocalDate nextEUControl
) {
}

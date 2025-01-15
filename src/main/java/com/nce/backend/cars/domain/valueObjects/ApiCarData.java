package com.nce.backend.cars.domain.valueObjects;

import java.time.LocalDate;


public record ApiCarData(
         String make,

         String model,

         LocalDate firstTimeRegisteredInNorway,

         String engineType,

         Integer engineVolume,

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

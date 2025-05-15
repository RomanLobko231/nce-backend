package com.nce.backend.cars.ui.requests;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import com.nce.backend.cars.domain.valueObjects.Status;
import org.springframework.stereotype.Service;

@Service
public class CarRequestMapper {

    public Car toCarFromUpdateRequest(UpdateCarRequest request) {
        return Car.builder()
                .id(request.id())
                .make(request.make())
                .model(request.model())
                .engineType(request.engineType())
                .engineVolume(request.engineVolume())
                .bodywork(request.bodywork())
                .kilometers(request.kilometers())
                .registrationNumber(request.registrationNumber())
                .color(request.color())
                .weight(request.weight())
                .gearboxType(GearboxType.fromString(request.gearboxType()))
                .operatingMode(OperatingMode.fromString(request.operatingMode()))
                .firstTimeRegisteredInNorway(request.firstTimeRegisteredInNorway())
                .nextEUControl(request.nextEUControl())
                .numberOfSeats(request.numberOfSeats())
                .numberOfDoors(request.numberOfDoors())
                .ownerID(request.ownerId())
                .additionalInformation(request.additionalInformation())
                .imagePaths(request.imagePaths())
                .expectedPrice(request.expectedPrice())
                .build();
    }

    public Car toCarFromAdminRequest(AddCarAdminRequest request) {
        return Car.builder()
                .make(request.make())
                .model(request.model())
                .engineType(request.engineType())
                .engineVolume(request.engineVolume())
                .bodywork(request.bodywork())
                .kilometers(request.kilometers())
                .registrationNumber(request.registrationNumber())
                .color(request.color())
                .weight(request.weight())
                .gearboxType(GearboxType.fromString(request.gearboxType()))
                .operatingMode(OperatingMode.fromString(request.operatingMode()))
                .firstTimeRegisteredInNorway(request.firstTimeRegisteredInNorway())
                .nextEUControl(request.nextEUControl())
                .numberOfSeats(request.numberOfSeats())
                .numberOfDoors(request.numberOfDoors())
                .ownerID(request.ownerId())
                .additionalInformation(request.additionalInformation())
                .expectedPrice(request.expectedPrice())
                .imagePaths(request.imagePaths())
                .build();
    }

    public Car toCarFromCustomerRequest(AddCarCustomerRequest request) {
        return Car.builder()
                .make(request.make())
                .model(request.model())
                .engineType(request.engineType())
                .engineVolume(request.engineVolume())
                .bodywork(request.bodywork())
                .kilometers(request.kilometers())
                .registrationNumber(request.registrationNumber())
                .color(request.color())
                .weight(request.weight())
                .gearboxType(GearboxType.fromString(request.gearboxType()))
                .operatingMode(OperatingMode.fromString(request.operatingMode()))
                .firstTimeRegisteredInNorway(request.firstTimeRegisteredInNorway())
                .nextEUControl(request.nextEUControl())
                .numberOfSeats(request.numberOfSeats())
                .numberOfDoors(request.numberOfDoors())
                .ownerID(request.ownerId())
                .additionalInformation(request.additionalInformation())
                .expectedPrice(request.expectedPrice())
                .build();
    }
    
    public Car toCarFromCustomerSimpleRequest(AddCarSimplifiedRequest request) {
        return Car.builder()
                .registrationNumber(request.registrationNumber())
                .ownerID(request.ownerId())
                .kilometers(request.kilometers())
                .expectedPrice(request.expectedPrice())
                .status(Status.IN_REVIEW)
                .build();
    }
}

package com.nce.backend.cars.ui.requests;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.cars.ui.responses.CarResponse;
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
                .status(Status.fromString(request.status()))
                .ownerInfo(
                        new OwnerInfo(
                                request.ownerInfo().name(),
                                request.ownerInfo().phoneNumber(),
                                request.ownerInfo().email()
                        )
                )
                .additionalInformation(request.additionalInformation())
                .imagePaths(request.imagePaths())
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
                .status(Status.fromString(request.status()))
                .ownerInfo(
                        new OwnerInfo(
                                request.ownerInfo().name(),
                                request.ownerInfo().phoneNumber(),
                                request.ownerInfo().email()
                        )
                )
                .additionalInformation(request.additionalInformation())
                .imagePaths(request.imagePaths())
                .build();
    }
    
    public Car toCarFromCustomerRequest(AddCarCustomerRequest request) {
        return Car.builder()
                .registrationNumber(request.carRegistrationNumber())
                .ownerInfo(
                        OwnerInfo.builder()
                                .name(request.ownerName())
                                .phoneNumber(request.phoneNumber())
                                .email(request.email())
                                .build()
                )
                .kilometers(request.kilometers())
                .status(Status.IN_REVIEW)
                .build();
    }
}

package com.nce.backend.cars.ui.responses;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import org.springframework.stereotype.Service;

@Service
public class CarResponseMapper {

    public CarResponse toCarResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .make(car.getMake())
                .model(car.getModel())
                .engineType(car.getEngineType())
                .engineVolume(car.getEngineVolume())
                .bodywork(car.getBodywork())
                .kilometers(car.getKilometers())
                .registrationNumber(car.getRegistrationNumber())
                .color(car.getColor())
                .weight(car.getWeight())
                .gearboxType(car.getGearboxType().getValue())
                .operatingMode(car.getOperatingMode().getValue())
                .firstTimeRegisteredInNorway(car.getFirstTimeRegisteredInNorway())
                .nextEUControl(car.getNextEUControl())
                .numberOfSeats(car.getNumberOfSeats())
                .numberOfDoors(car.getNumberOfDoors())
                .status(car.getStatus().getValue())
                .ownerInfo(
                        new OwnerInfo(
                                car.getOwnerInfo().name(),
                                car.getOwnerInfo().phoneNumber(),
                                car.getOwnerInfo().email()
                        )
                )
                .additionalInformation(car.getAdditionalInformation())
                .imagePaths(car.getImagePaths())
                .build();
    }
}

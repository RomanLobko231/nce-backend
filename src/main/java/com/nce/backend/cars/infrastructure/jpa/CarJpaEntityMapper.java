package com.nce.backend.cars.infrastructure.jpa;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import org.springframework.stereotype.Service;

@Service
public class CarJpaEntityMapper {

    public CarJpaEntity toJpaEntity(Car car) {
        return CarJpaEntity.builder()
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
                .gearboxType(car.getGearboxType())
                .operatingMode(car.getOperatingMode())
                .firstTimeRegisteredInNorway(car.getFirstTimeRegisteredInNorway())
                .nextEUControl(car.getNextEUControl())
                .numberOfSeats(car.getNumberOfSeats())
                .numberOfDoors(car.getNumberOfDoors())
                .status(car.getStatus())
                .ownerInfo(
                        new OwnerInfoJpa(
                                car.getOwnerInfo().name(),
                                car.getOwnerInfo().phoneNumber(),
                                car.getOwnerInfo().email()
                        )
                )
                .additionalInformation(car.getAdditionalInformation())
                .imagePaths(car.getImagePaths())
                .build();
    }

    public Car toDomainEntity(CarJpaEntity carJpaEntity) {
        return Car.builder()
                .id(carJpaEntity.getId())
                .make(carJpaEntity.getMake())
                .model(carJpaEntity.getModel())
                .engineType(carJpaEntity.getEngineType())
                .engineVolume(carJpaEntity.getEngineVolume())
                .bodywork(carJpaEntity.getBodywork())
                .kilometers(carJpaEntity.getKilometers())
                .registrationNumber(carJpaEntity.getRegistrationNumber())
                .color(carJpaEntity.getColor())
                .weight(carJpaEntity.getWeight())
                .gearboxType(carJpaEntity.getGearboxType())
                .operatingMode(carJpaEntity.getOperatingMode())
                .firstTimeRegisteredInNorway(carJpaEntity.getFirstTimeRegisteredInNorway())
                .nextEUControl(carJpaEntity.getNextEUControl())
                .numberOfSeats(carJpaEntity.getNumberOfSeats())
                .numberOfDoors(carJpaEntity.getNumberOfDoors())
                .status(carJpaEntity.getStatus())
                .ownerInfo(
                        new OwnerInfo(
                                carJpaEntity.getOwnerInfo().name,
                                carJpaEntity.getOwnerInfo().phoneNumber,
                                carJpaEntity.getOwnerInfo().email
                        )
                )
                .additionalInformation(carJpaEntity.getAdditionalInformation())
                .imagePaths(carJpaEntity.getImagePaths())
                .build();
    }
}

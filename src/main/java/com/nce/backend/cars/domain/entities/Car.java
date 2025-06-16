package com.nce.backend.cars.domain.entities;

import com.nce.backend.cars.domain.valueObjects.*;
import jakarta.persistence.ElementCollection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
public class Car {

    private UUID id;

    private String registrationNumber;

    private Integer kilometers;

    private String make;

    private String model;

    private LocalDate firstTimeRegisteredInNorway;

    private String engineType;

    private Double engineVolume;

    private String bodywork;

    private Integer numberOfSeats;

    private Integer numberOfDoors;

    private String color;

    private GearboxType gearboxType;

    private OperatingMode operatingMode;

    private Integer weight;

    private LocalDate nextEUControl;

    private UUID ownerID;

    private Status status;

    private String additionalInformation;

    private Integer expectedPrice;

    @Builder.Default
    private List<String> imagePaths = new ArrayList<>();

    public void addNewImagePaths(List<String> newImagePaths) {
        if (newImagePaths != null && !imagePaths.contains(null)) {
            this.imagePaths.addAll(newImagePaths);
        }
    }

    public void updateDataFromApi(ApiCarData apiData){
        this.setMake(apiData.make());
        this.setModel(apiData.model());
        this.setColor(apiData.color());
        this.setEngineType(apiData.engineType());
        this.setEngineVolume(apiData.engineVolume());
        this.setFirstTimeRegisteredInNorway(apiData.firstTimeRegisteredInNorway());
        this.setNextEUControl(apiData.nextEUControl());
        this.setGearboxType(apiData.gearboxType());
        this.setNumberOfDoors(apiData.numberOfDoors());
        this.setNumberOfSeats(apiData.numberOfSeats());
        this.setBodywork(apiData.bodywork());
        this.setOperatingMode(apiData.operatingMode());
        this.setWeight(apiData.weight());
    }

}

package com.nce.backend.cars.domain.entities;

import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
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

    private Integer engineVolume;

    private String bodywork;

    private Integer numberOfSeats;

    private Integer numberOfDoors;

    private String color;

    private GearboxType gearboxType;

    private OperatingMode operatingMode;

    private Integer weight;

    private LocalDate nextEUControl;

    private OwnerInfo ownerInfo;

    private Status status;

    private String additionalInformation;

    private List<String> imagePaths = new ArrayList<>();

}

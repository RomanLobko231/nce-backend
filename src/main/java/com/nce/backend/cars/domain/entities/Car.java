package com.nce.backend.cars.domain.entities;

import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.domain.valueObjects.Status;
import jakarta.persistence.ElementCollection;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Car {

    private UUID id;

    private String registrationNumber;

    private Integer kilometers;

    private String make;

    private String model;

    private LocalTime firstTimeRegisteredInNorway;

    private String engineType;

    private Integer engineVolume;

    private String bodywork;

    private Integer numberOfSeats;

    private Integer numberOfDoors;

    private String color;

    private GearboxType gearboxType;

    private Integer weight;

    private LocalDate nextEUControl;

    //@Embeddable
    private OwnerInfo ownerInfo;

    private Status status;

    private String additionalInformation;

    //@ElementCollection
    private List<String> imagesPaths = new ArrayList<>();

}

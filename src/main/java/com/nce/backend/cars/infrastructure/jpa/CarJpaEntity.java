package com.nce.backend.cars.infrastructure.jpa;

import com.nce.backend.cars.domain.valueObjects.GearboxType;
import com.nce.backend.cars.domain.valueObjects.OperatingMode;
import com.nce.backend.cars.domain.valueObjects.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "car")
@Table(name = "car")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CarJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Registration number can't be null")
    @NotBlank(message = "Registration number can't be null")
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

    @Enumerated(EnumType.STRING)
    private GearboxType gearboxType;

    @Enumerated(EnumType.STRING)
    private OperatingMode operatingMode;

    private Integer weight;

    @Column(name = "next_eu_control")
    private LocalDate nextEUControl;

    private UUID ownerId;

    @NotNull(message = "Status can't be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    private String additionalInformation;

    @ElementCollection
    @CollectionTable(
            name = "car_image_paths",
            joinColumns = @JoinColumn(name = "car_id")
    )
    @Column(name = "image_path")
    @Builder.Default
    private List<String> imagePaths = new ArrayList<>();
}

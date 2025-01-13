package com.nce.backend.cars.domain.valueObjects;

import lombok.Getter;

@Getter
public enum OperatingMode {

    REAR_WHEEL("Bakhjulstrekk"),

    FRONT_WHEEL("Framhjulstrekk"),

    FOUR_WHEEL("Firehjulstrekk"),

    OTHER("Annet");

    private final String value;

    OperatingMode(String value) {
        this.value = value;
    }

}

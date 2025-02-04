package com.nce.backend.cars.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    public static OperatingMode fromString(String apiValue) {
        for (OperatingMode type : values()) {
            if (type.value.equalsIgnoreCase(apiValue)) {
                return type;
            }
        }
        return OTHER;
    }

}

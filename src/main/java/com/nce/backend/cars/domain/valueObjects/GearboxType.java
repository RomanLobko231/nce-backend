package com.nce.backend.cars.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum GearboxType {

    MANUAL("Manuell"),

    AUTOMATIC("Automat"),

    OTHER("Annet");

    private final String value;

    GearboxType(String value){
        this.value = value;
    }

    public static GearboxType fromString(String apiValue) {
        for (GearboxType type : values()) {
            if (type.value.equalsIgnoreCase(apiValue)) {
                return type;
            }
        }
        return OTHER;
    }
}

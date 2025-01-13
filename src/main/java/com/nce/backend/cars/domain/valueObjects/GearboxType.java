package com.nce.backend.cars.domain.valueObjects;

import lombok.Getter;

@Getter
public enum GearboxType {

    MANUAL("Manuel"),

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

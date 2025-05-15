package com.nce.backend.cars.domain.valueObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Status {

    IN_REVIEW("Vurdering"),

    ON_AUCTION("Auksjon"),

    SOLD("Solgt"),

    OTHER("Annet");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status fromString(String value) {
        for (Status type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return OTHER;
    }

}

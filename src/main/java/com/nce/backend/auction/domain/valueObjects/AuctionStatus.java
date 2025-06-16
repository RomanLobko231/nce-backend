package com.nce.backend.auction.domain.valueObjects;

import lombok.Getter;

@Getter
public enum AuctionStatus {
    ACTIVE("Aktivt"),
    FINISHED("Avsluttet"),
    DISABLED("Deaktivert");

    private final String value;

    AuctionStatus(String value) {
        this.value = value;
    }

    public static AuctionStatus fromString(String value) {
        for (AuctionStatus type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Auction Status: " + value);
    }
}

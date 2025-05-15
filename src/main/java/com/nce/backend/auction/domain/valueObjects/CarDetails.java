package com.nce.backend.auction.domain.valueObjects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class CarDetails {
    private UUID carId;

    private String thumbnailImageUrl;

    private String makeModel;

    private String modelYear;
}

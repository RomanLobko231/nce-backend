package com.nce.backend.auction.ui.rest.requests;

import com.nce.backend.common.validation.InstantInFuture;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record NewAuctionRequest(

        @NotNull(message = "Starting price cannot be null")
        @DecimalMin(value = "0", message = "Starting price cannot me less than 0")
        BigDecimal startingPrice,

        @NotNull(message = "Minimal step cannot be null")
        @DecimalMin(value = "0", message = "Minimal step cannot me less than 0")
        BigDecimal minimalStep,

        @NotNull(message = "Expected price cannot be null")
        @DecimalMin(value = "0", message = "Expected price cannot me less than 0")
        BigDecimal expectedPrice,

        @NotNull(message = "Auction end time cannot be null")
        Instant endDateTime,

        @NotNull(message = "Car Id cannot be null")
        UUID carId,

        @NotBlank(message = "Image for car cannot be absent")
        String thumbnailImageUrl,

        @NotBlank(message = "Make and model of car cannot be absent")
        String makeModel,

        @NotBlank(message = "Model year cannot be absent")
        String modelYear
) {
}

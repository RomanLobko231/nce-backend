package com.nce.backend.auction.ui.rest.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UpdateAuctionRequest(

        @NotNull(message = "Auction Id cannot be null")
        UUID id,

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
        Instant endDateTime
) {
}

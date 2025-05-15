package com.nce.backend.auction.ui.rest.responses;

import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.valueObjects.CarDetails;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record AuctionResponse(
        UUID id,

        BigDecimal startingPrice,

        BigDecimal minimalStep,

        BigDecimal expectedPrice,

        Bid highestBid,

        Instant endDateTime,

        CarDetails carDetails,

        String status
) {
}

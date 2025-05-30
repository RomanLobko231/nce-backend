package com.nce.backend.auction.ui.websocket.responses;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.valueObjects.CarDetails;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record AuctionUpdatedResponse(
        UUID id,

        BigDecimal startingPrice,

        BigDecimal minimalStep,

        BigDecimal expectedPrice,

        Bid highestBid,

        Instant endDateTime,

        CarDetails carDetails,

        String status,

        List<Bid> bids
) {

    static public AuctionUpdatedResponse fromDomain(Auction auction) {
        return AuctionUpdatedResponse
                .builder()
                .id(auction.getId())
                .startingPrice(auction.getStartingPrice())
                .minimalStep(auction.getMinimalStep())
                .expectedPrice(auction.getExpectedPrice())
                .highestBid(auction.getHighestBid())
                .endDateTime(auction.getEndDateTime())
                .carDetails(auction.getCarDetails())
                .status(auction.getStatus().name())
                .bids(auction.getBids())
                .build();
    }
}

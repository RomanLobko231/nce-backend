package com.nce.backend.auction.ui.rest.responses;

import com.nce.backend.auction.domain.entities.Auction;
import org.springframework.stereotype.Service;

@Service
public class AuctionResponseMapper {

    public AuctionResponse toAuctionResponse(Auction auction) {
        return AuctionResponse
                .builder()
                .id(auction.getId())
                .highestBid(auction.getHighestBid())
                .startingPrice(auction.getStartingPrice())
                .expectedPrice(auction.getExpectedPrice())
                .endDateTime(auction.getEndDateTime())
                .minimalStep(auction.getMinimalStep())
                .carDetails(auction.getCarDetails())
                .status(auction.getStatus().getValue())
                .build();
    }
}

package com.nce.backend.auction.ui.rest.responses;

import com.nce.backend.auction.domain.entities.Auction;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<AuctionResponse> toAuctionResponses(List<Auction> auctions) {
        return auctions
                .stream()
                .map(this::toAuctionResponse)
                .toList();
    }
}

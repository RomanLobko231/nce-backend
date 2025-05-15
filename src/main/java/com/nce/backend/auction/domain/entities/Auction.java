package com.nce.backend.auction.domain.entities;

import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.valueObjects.CarDetails;
import com.nce.backend.auction.exceptions.AuctionClosedException;
import com.nce.backend.auction.exceptions.InvalidBidException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
public class Auction {
    private UUID id;

    private BigDecimal startingPrice;

    private BigDecimal minimalStep;

    private BigDecimal expectedPrice;

    private Bid highestBid;

    private Instant endDateTime;

    private CarDetails carDetails;

    @Builder.Default
    private List<Bid> bids = new ArrayList<>();

    private AuctionStatus status;

    public void placeNewBid(Bid bid) {
        if (bid == null) {
            throw new InvalidBidException("Bid cannot be null");
        }

        this.checkAuctionAvailability();

        BigDecimal minAcceptableAmount = bids.isEmpty()
                ? this.startingPrice
                : this.highestBid.getAmount().add(minimalStep);

        if (bid.getAmount().compareTo(minAcceptableAmount) < 0) {
            throw new InvalidBidException("Bid is too low. Must be at least %.2f".formatted(minAcceptableAmount));
        }

        bid.setPlacedAt(Instant.now());
        this.endDateTime = endDateTime.plus(5, ChronoUnit.MINUTES);
        this.bids.add(bid);
        this.highestBid = bid;
    }

    public void applyChangesFrom(Auction auction) {
        if (auction == null || auction.getId() == null) {
            throw new IllegalArgumentException("Auction cannot be null");
        }

        if (auction.getEndDateTime() != null) this.endDateTime = auction.getEndDateTime();
        if (auction.getCarDetails() != null) this.carDetails = auction.getCarDetails();
        if (auction.getExpectedPrice() != null) this.expectedPrice = auction.getExpectedPrice();
        if (auction.getMinimalStep() != null) this.minimalStep = auction.getMinimalStep();
        if (auction.getStartingPrice() != null) this.startingPrice = auction.getStartingPrice();
    }

    public void checkAuctionAvailability(){
        if(Instant.now().isAfter(endDateTime) ||
                status == AuctionStatus.FINISHED ) {
            throw new AuctionClosedException("Auction is already closed. Cannot place new bid.");
        }
        if (status == AuctionStatus.DISABLED) {
            throw new AuctionClosedException("Auction is currently paused.");
        }
    }
}

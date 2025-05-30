package com.nce.backend.auction.domain.entities;

import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.AutoBid;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.valueObjects.CarDetails;
import com.nce.backend.auction.exceptions.AuctionClosedException;
import com.nce.backend.auction.exceptions.InvalidBidException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

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

    @Builder.Default
    private List<AutoBid> autoBids = new ArrayList<>();

    private AuctionStatus status;

    public void placeNewBid(Bid bid) {
        this.checkAuctionAvailability();
        this.validateBid(bid);

        // Extend auction time by 2 minutes due to last-minute bids
        if (Duration.between(Instant.now(), endDateTime).toMinutes() < 10) {
            endDateTime = endDateTime.plus(2, ChronoUnit.MINUTES);
        }

        bid.setPlacedAt(Instant.now());
        bids.add(bid);
        highestBid = bid;
    }

    public void placeNewAutoBid(AutoBid autoBid) {
        this.validateAutoBid(autoBid);

        Bid bid = Bid
                .builder()
                .bidderId(autoBid.getBidderId())
                .auctionId(this.getId())
                .amount(highestBid.getAmount().add(minimalStep))
                .build();

        this.placeNewBid(bid);

        autoBid.setPlacedAt(Instant.now());
        autoBids.add(autoBid);
    }

    private void validateBid(Bid bid) {
        if (bid == null) {
            throw new InvalidBidException("Bid cannot be null");
        }

        BigDecimal minAcceptableAmount = bids.isEmpty()
                ? startingPrice
                : highestBid.getAmount().add(minimalStep);

        if (bid.getAmount().compareTo(minAcceptableAmount) < 0) {
            throw new InvalidBidException("Bid is too low. Must be at least %.2f".formatted(minAcceptableAmount));
        }
    }

    private void validateAutoBid(AutoBid autoBid) {
        if (autoBid == null) {
            throw new InvalidBidException("Auto Bid cannot be null");
        }

        BigDecimal minAcceptableAmount = bids.isEmpty()
                ? startingPrice
                : highestBid.getAmount().add(minimalStep);

        if (autoBid.getLimitAmount().compareTo(minAcceptableAmount) < 0) {
            throw new InvalidBidException(
                    "Auto Bid is too low. Must be at least %.2f".formatted(minAcceptableAmount)
            );
        }
    }

    public void checkAuctionAvailability() {
        if (Instant.now().isAfter(endDateTime) ||
                this.status == AuctionStatus.FINISHED) {
            throw new AuctionClosedException("Auction is already closed. Cannot place new bid.");
        }
        if (this.status == AuctionStatus.DISABLED) {
            throw new AuctionClosedException("Auction is currently paused.");
        }
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


    public void triggerAutoBids() {
        boolean placed;
        do {
            placed = false;

            Optional<AutoBid> nextAutoBid = autoBids
                    .stream()
                    .filter(autoBid -> autoBid
                            .getLimitAmount()
                            .compareTo(highestBid.getAmount().add(minimalStep)) >= 0)
                    .filter(autoBid -> !autoBid
                            .getBidderId()
                            .equals(highestBid.getBidderId()))
                    .min(Comparator
                            .comparing(AutoBid::getLimitAmount)
                            .thenComparing(AutoBid::getPlacedAt));

            if (nextAutoBid.isPresent()) {
                placeNewBid(Bid
                                .builder()
                                .amount(highestBid.getAmount().add(minimalStep))
                                .auctionId(id)
                                .bidderId(nextAutoBid.get().getBidderId())
                                .build());
                placed = true;
            }

        } while (placed);
    }

    public void restartWithNewData(Auction newAuctionData) {
        if (newAuctionData.getEndDateTime().isBefore(Instant.now())) {
            throw new IllegalStateException(
                    "New end date should be in future. Your is '%s'".formatted(newAuctionData.getEndDateTime().toString())
            );
        }
        if (this.status == AuctionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot restart. Auction has already been started");
        }

        applyChangesFrom(newAuctionData);
        this.status = AuctionStatus.ACTIVE;
    }

}

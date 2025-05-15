package com.nce.backend.auction.ui.websocket.requests;

import com.nce.backend.auction.domain.valueObjects.Bid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record BidMessage(

        @NotNull(message = "Auction Id cannot be null")
        UUID auctionId,

        @NotNull(message = "Bidder Id cannot be null")
        UUID bidderId,

        @NotNull
        @Min(value = 0, message = "AMount must be no less than 0")
        BigDecimal amount) {

    public Bid toDomain() {
        return Bid
                .builder()
                .auctionId(auctionId)
                .bidderId(bidderId)
                .amount(amount)
                .build();
    }
}

package com.nce.backend.auction.ui.websocket.requests;

import com.nce.backend.auction.domain.valueObjects.AutoBid;
import com.nce.backend.auction.domain.valueObjects.Bid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AutoBidMessage(
        @NotNull(message = "Auction Id cannot be null")
        UUID auctionId,

        @NotNull
        @Min(value = 0, message = "AMount must be no less than 0")
        BigDecimal limitAmount
) {
    public AutoBid toDomain() {
        return AutoBid
                .builder()
                .auctionId(this.auctionId)
                .limitAmount(this.limitAmount)
                .build();
    }
}

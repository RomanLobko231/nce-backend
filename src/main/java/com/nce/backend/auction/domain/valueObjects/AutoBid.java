package com.nce.backend.auction.domain.valueObjects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AutoBid {
    private UUID auctionId;

    private UUID bidderId;

    private BigDecimal limitAmount;

    private Instant placedAt;
}

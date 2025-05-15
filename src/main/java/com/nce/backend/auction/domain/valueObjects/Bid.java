package com.nce.backend.auction.domain.valueObjects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Setter
@Getter
public class Bid {
    private UUID auctionId;

    private UUID bidderId;

    private BigDecimal amount;

    private Instant placedAt;
}

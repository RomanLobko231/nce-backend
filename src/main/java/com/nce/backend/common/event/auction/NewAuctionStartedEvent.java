package com.nce.backend.common.event.auction;

import java.time.Instant;
import java.util.UUID;

public record NewAuctionStartedEvent(
        UUID auctionId,
        UUID carId,
        Instant endDateTime
) {
}

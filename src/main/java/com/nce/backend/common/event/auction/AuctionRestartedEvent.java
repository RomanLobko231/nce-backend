package com.nce.backend.common.event.auction;

import java.time.Instant;
import java.util.UUID;

public record AuctionRestartedEvent(
        UUID carId,
        UUID auctionId,
        Instant newEndDateTime
) {
}

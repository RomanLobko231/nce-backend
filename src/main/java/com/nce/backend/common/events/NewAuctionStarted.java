package com.nce.backend.common.events;

import java.time.Instant;
import java.util.UUID;

public record NewAuctionStarted(
        UUID auctionId,
        UUID carId,
        Instant endDateTime
) {
}

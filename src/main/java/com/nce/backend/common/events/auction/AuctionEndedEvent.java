package com.nce.backend.common.events.auction;

import java.util.UUID;

public record AuctionEndedEvent(
        UUID auctionId,
        UUID carId
) {
}

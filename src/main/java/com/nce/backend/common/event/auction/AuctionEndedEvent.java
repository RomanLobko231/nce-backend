package com.nce.backend.common.event.auction;

import java.util.UUID;

public record AuctionEndedEvent(
        UUID auctionId,
        UUID carId
) {
}

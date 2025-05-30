package com.nce.backend.common.events.auction;

import java.util.UUID;

public record NewBidPlacedEvent(
        UUID bidderId,
        UUID carId
) {
}

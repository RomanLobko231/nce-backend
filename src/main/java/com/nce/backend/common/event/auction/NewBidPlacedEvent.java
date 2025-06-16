package com.nce.backend.common.event.auction;

import java.util.UUID;

public record NewBidPlacedEvent(
        UUID bidderId,
        UUID carId
) {
}

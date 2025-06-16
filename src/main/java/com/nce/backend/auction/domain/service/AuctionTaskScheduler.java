package com.nce.backend.auction.domain.service;

import java.time.Instant;
import java.util.UUID;

public interface AuctionTaskScheduler {
    void scheduleAuctionFinish(UUID auctionId, Instant finishTime);
    void restoreScheduledFinishes();
}

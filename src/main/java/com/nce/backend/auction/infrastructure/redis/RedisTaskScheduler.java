package com.nce.backend.auction.infrastructure.redis;

import com.nce.backend.auction.domain.repository.AuctionRepository;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.common.events.NewAuctionStarted;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedisTaskScheduler {

    private final StringRedisTemplate redisTemplate;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final AuctionRepository auctionRepository;

    private static final String AUCTION_JOB_KEY = "auction_job_";

    @Async(("eventTaskExecutor"))
    @TransactionalEventListener
    public void onAuctionCreated(NewAuctionStarted event) {
        if (isPastDeadline(event.endDateTime())) {
            return;
        }

        String jobKey = AUCTION_JOB_KEY + event.auctionId();

        redisTemplate
                .opsForValue()
                .set(jobKey, event.endDateTime().toString());

        taskScheduler.schedule(() -> auctionRepository
                .findById(event.auctionId())
                .ifPresent(auction -> {
                    if (auction.getStatus().equals(AuctionStatus.ACTIVE) &&
                            !isPastDeadline(auction.getEndDateTime())){
                        this.scheduleStatusChange(event.auctionId(), auction.getEndDateTime());
                        return;
                    }

                    if (auction.getStatus().equals(AuctionStatus.ACTIVE) &&
                            isPastDeadline(event.endDateTime())) {
                        auctionRepository.updateAuctionStatusById(AuctionStatus.FINISHED, auction.getId());
                    }

                    redisTemplate.delete(jobKey);
                }), event.endDateTime().atZone(ZoneId.systemDefault()).toInstant());
    }

    @PostConstruct
    public void restoreScheduledAuctions() {
        Set<String> jobKeys = redisTemplate.keys(AUCTION_JOB_KEY + "*");

        if (jobKeys == null || jobKeys.isEmpty()) return;

        for (String jobKey : jobKeys) {
            String auctionIdStr = jobKey.replace(AUCTION_JOB_KEY, "");
            String endDateTimeStr = redisTemplate.opsForValue().get(jobKey);

            if (endDateTimeStr == null) continue;

            Instant endDateTime = Instant.parse(endDateTimeStr);

            if (isPastDeadline(endDateTime)) {
                redisTemplate.delete(jobKey);
                continue;
            }

            UUID auctionId = UUID.fromString(auctionIdStr);

            taskScheduler.schedule(() -> auctionRepository
                    .findById(auctionId)
                    .ifPresent(auction -> {
                        if (auction.getStatus().equals(AuctionStatus.ACTIVE) &&
                                isPastDeadline(auction.getEndDateTime())) {
                            auctionRepository.updateAuctionStatusById(AuctionStatus.FINISHED, auction.getId());
                        }

                        redisTemplate.delete(jobKey);
                    }), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    private void scheduleStatusChange(UUID auctionId, Instant endDateTime) {
        String jobKey = AUCTION_JOB_KEY + auctionId;

        redisTemplate
                .opsForValue()
                .set(jobKey, endDateTime.toString());

        taskScheduler.schedule(() -> auctionRepository
                .findById(auctionId)
                .ifPresent(auction -> {
                    if (auction.getStatus().equals(AuctionStatus.ACTIVE) &&
                            isPastDeadline(auction.getEndDateTime())) {
                        auctionRepository.updateAuctionStatusById(AuctionStatus.FINISHED, auction.getId());
                    }

                    redisTemplate.delete(jobKey);
                }), endDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private boolean isPastDeadline(Instant deadline) {
        return Instant.now().isAfter(deadline);
    }
}

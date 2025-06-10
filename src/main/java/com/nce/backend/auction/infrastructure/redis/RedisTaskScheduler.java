package com.nce.backend.auction.infrastructure.redis;

import com.nce.backend.auction.domain.repository.AuctionRepository;
import com.nce.backend.auction.domain.service.AuctionTaskScheduler;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.common.event.auction.AuctionEndedEvent;
import com.nce.backend.common.event.auction.AuctionRestartedEvent;
import com.nce.backend.common.event.auction.NewAuctionStartedEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
public class RedisTaskScheduler implements AuctionTaskScheduler {

    private final StringRedisTemplate redisTemplate;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final AuctionRepository auctionRepository;
    private final ApplicationEventPublisher eventPublisher;

    private static final String AUCTION_JOB_KEY = "auction_job_";

    @Async(("eventTaskExecutor"))
    @TransactionalEventListener
    public void onAuctionCreated(NewAuctionStartedEvent event) {
        this.scheduleAuctionFinish(event.auctionId(), event.endDateTime());
    }

    @Async(("eventTaskExecutor"))
    @TransactionalEventListener
    public void onAuctionRestarted(AuctionRestartedEvent event) {
        String jobKey = AUCTION_JOB_KEY + event.auctionId();

        String endDateTimeStr = redisTemplate.opsForValue().get(jobKey);

        if (endDateTimeStr != null && !endDateTimeStr.isEmpty()) {
            Instant existingDeadline = Instant.parse(endDateTimeStr);

            if (isDeadlineInFuture(existingDeadline) && existingDeadline.isBefore(event.newEndDateTime())) {
                return;
            }
        }

        this.scheduleAuctionFinish(event.auctionId(), event.newEndDateTime());
    }


    //gives the ability to recursively set new task for ending auction if deadline has been changed
    @Override
    public void scheduleAuctionFinish(UUID auctionId, Instant finishTime) {
        if (isDeadlinePassed(finishTime)) {
            return;
        }

        String jobKey = AUCTION_JOB_KEY + auctionId;

        redisTemplate
                .opsForValue()
                .set(jobKey, finishTime.toString());

        taskScheduler.schedule(() -> auctionRepository
                .findById(auctionId)
                .ifPresent(auction -> {
                    if (auction.getStatus() == AuctionStatus.ACTIVE &&
                            !isDeadlinePassed(auction.getEndDateTime())) {
                        this.scheduleAuctionFinish(auctionId, auction.getEndDateTime());
                        return;
                    }
                    if (auction.getStatus() == AuctionStatus.ACTIVE &&
                            isDeadlinePassed(auction.getEndDateTime())) {
                        auctionRepository.updateAuctionStatusById(AuctionStatus.FINISHED, auction.getId());
                        redisTemplate.delete(jobKey);

                        eventPublisher.publishEvent(
                                new AuctionEndedEvent(auction.getId(), auction.getCarDetails().getCarId())
                        );
                    }

                }), finishTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    @PostConstruct
    public void restoreScheduledFinishes() {
        Set<String> jobKeys = redisTemplate.keys(AUCTION_JOB_KEY + "*");

        if (jobKeys == null || jobKeys.isEmpty()) return;

        for (String jobKey : jobKeys) {
            String auctionIdStr = jobKey.replace(AUCTION_JOB_KEY, "");
            String endDateTimeStr = redisTemplate.opsForValue().get(jobKey);

            if (endDateTimeStr == null) continue;

            Instant endDateTime = Instant.parse(endDateTimeStr);
            UUID auctionId = UUID.fromString(auctionIdStr);

            if (isDeadlinePassed(endDateTime)) {
                auctionRepository
                        .findById(auctionId)
                        .ifPresent(auction -> {
                            auctionRepository.updateAuctionStatusById(AuctionStatus.FINISHED, auctionId);
                            eventPublisher.publishEvent(
                                    new AuctionEndedEvent(auction.getId(), auction.getCarDetails().getCarId())
                            );
                        });

                redisTemplate.delete(jobKey);
                continue;
            }

            this.scheduleAuctionFinish(auctionId, endDateTime);
        }
    }

    private boolean isDeadlinePassed(Instant deadline) {
        return Instant.now().isAfter(deadline);
    }

    private boolean isDeadlineInFuture(Instant deadline) {
        return Instant.now().isBefore(deadline);
    }
}

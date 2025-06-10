package com.nce.backend.auctions;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.service.AuctionDomainService;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.valueObjects.CarDetails;
import com.nce.backend.auction.infrastructure.redis.RedisTaskScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AuctionIntegrationTest {
    @Autowired
    private AuctionDomainService auctionService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @MockitoSpyBean
    private RedisTaskScheduler redisTaskScheduler;

    @Test
    void auctionIsFinishedAfterEndTime() throws InterruptedException {

        Auction auction = Auction
                .builder()
                .highestBid(Bid
                        .builder()
                        .amount(BigDecimal.valueOf(1000))
                        .auctionId(UUID.randomUUID())
                        .bidderId(UUID.randomUUID())
                        .build()
                )
                .carDetails(
                        CarDetails
                                .builder()
                                .makeModel("make")
                                .thumbnailImageUrl("img")
                                .modelYear("1000")
                                .carId(UUID.randomUUID())
                                .build()
                )
                .minimalStep(BigDecimal.valueOf(1000))
                .expectedPrice(BigDecimal.valueOf(1000))
                .startingPrice(BigDecimal.valueOf(1000))
                .build();

        auction.setEndDateTime(Instant.now().plusSeconds(3));
        auctionService.startAuction(auction);

        Thread.sleep(5000);

        Auction updated = auctionService
                .getAuctionByCarId(auction.getCarDetails().getCarId());

        assertNull(redisTemplate.opsForValue().get("auction_job_" + auction.getId()));
        assertEquals(AuctionStatus.FINISHED, updated.getStatus());
    }



    @Test
    void listenerShouldBeInvokedOnAuctionCreatedEvent() {
        Auction auction = Auction.builder()
                .highestBid(Bid
                        .builder()
                        .amount(BigDecimal.valueOf(1000))
                        .auctionId(UUID.randomUUID())
                        .bidderId(UUID.randomUUID())
                        .build())
                .carDetails(
                        CarDetails
                                .builder()
                                .makeModel("make")
                                .thumbnailImageUrl("img")
                                .modelYear("1000")
                                .carId(UUID.randomUUID())
                                .build()
                )
                .minimalStep(BigDecimal.valueOf(1000))
                .expectedPrice(BigDecimal.valueOf(1000))
                .startingPrice(BigDecimal.valueOf(1000))
                .build();

        auction.setEndDateTime(Instant.now().plusSeconds(5));

        auctionService.startAuction(auction);

        verify(redisTaskScheduler, timeout(1000)).onAuctionCreated(
                argThat(event -> event.carId().equals(auction.getCarDetails().getCarId()))
        );
    }

}

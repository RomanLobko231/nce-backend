package com.nce.backend.auction.infrastructure.jpa.entity;

import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity(name = "auction")
@Table(name = "auction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuctionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal startingPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal minimalStep;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal expectedPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "highest_bid_amount")),
            @AttributeOverride(name = "bidderId", column = @Column(name = "highest_bid_bidder_id")),
            @AttributeOverride(name = "placedAt", column = @Column(name = "highest_bid_placed_at")),
            @AttributeOverride(name = "bidDiscriminator", column = @Column(name = "highest_bid_bid_discriminator"))
    })
    private BidEmbeddable highestBid;

    @Column(nullable = false)
    private Instant endDateTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "auction_bids",
            joinColumns = @JoinColumn(name = "auction_id")
    )
    @Builder.Default
    private List<BidEmbeddable> bids = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "auction_auto_bids",
            joinColumns = @JoinColumn(name = "auction_id")
    )
    @Builder.Default
    private List<AutoBidEmbeddable> autoBids = new ArrayList<>();

    @Embedded
    private CarDetailsEmbeddable carDetails;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuctionStatus status;
}

package com.nce.backend.auction.infrastructure.jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BidEmbeddable {

    @Builder.Default
    private UUID bidDiscriminator = UUID.randomUUID();

    private UUID bidderId;

    private BigDecimal amount;

    private Instant placedAt;
}


package com.nce.backend.users.infrastructure.jpa.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "seller")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "seller_id")
public class SellerJpaEntity extends UserJpaEntity {

    @Embedded
    private AddressJpaEntity sellerAddress;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "seller_car_ids",
            joinColumns = @JoinColumn(name = "seller_id")
    )
    @Column(name = "car_id")
    @Builder.Default
    private List<UUID> carIDs = new ArrayList<>();
}

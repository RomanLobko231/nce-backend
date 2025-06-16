package com.nce.backend.users.infrastructure.jpa.entities.buyer;

import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity(name = "buyer_representative")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "buyer_representative_id")
public class BuyerRepresentativeJpaEntity extends UserJpaEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "representative_car_ids",
            joinColumns = @JoinColumn(name = "buyer_representative_id")
    )
    @Column(name = "car_id")
    @Builder.Default
    private Set<UUID> savedCarIds = new HashSet<>();

    @Column(nullable = false, name = "buyer_company_id")
    private UUID buyerCompanyId;
}

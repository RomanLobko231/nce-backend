package com.nce.backend.users.infrastructure.jpa.entities.buyer;

import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private List<UUID> savedCarIds = new ArrayList<>();

    @Column(nullable = false, name = "buyer_company_id")
    private UUID buyerCompanyId;
}

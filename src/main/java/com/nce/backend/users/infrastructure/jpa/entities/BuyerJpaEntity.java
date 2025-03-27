package com.nce.backend.users.infrastructure.jpa.entities;

import com.nce.backend.users.domain.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "buyer")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "buyer_id")
public class BuyerJpaEntity extends UserJpaEntity {

    @NotBlank(message = "Organisation name cannot be blank")
    @NotNull(message = "Organisation name cannot be null")
    private String organisationName;

    @NotBlank(message = "Organisation number cannot be blank")
    @NotNull(message = "Organisation number cannot be null")
    @Column(unique = true)
    private String organisationNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "org_licence_urls",
            joinColumns = @JoinColumn(name = "buyer_id")
    )
    @Column(name = "url")
    private List<String> organisationLicenceURLs;

    @Embedded
    private AddressJpaEntity organisationAddress;

}

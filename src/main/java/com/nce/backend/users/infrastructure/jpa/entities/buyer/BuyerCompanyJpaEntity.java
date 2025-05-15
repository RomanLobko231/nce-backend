package com.nce.backend.users.infrastructure.jpa.entities.buyer;

import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "buyer_company")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "buyer_company_id")
public class BuyerCompanyJpaEntity extends UserJpaEntity {

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
            joinColumns = @JoinColumn(name = "buyer_company_id")
    )
    @Column(name = "url")
    private List<String> organisationLicenceURLs;

    @Embedded
    private BuyerCompanyAddressJpaEntity organisationAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "buyer_company_id")
    private List<BuyerRepresentativeJpaEntity> representatives;

}

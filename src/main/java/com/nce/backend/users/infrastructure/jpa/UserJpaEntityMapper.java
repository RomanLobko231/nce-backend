package com.nce.backend.users.infrastructure.jpa;

import com.nce.backend.users.domain.entities.*;
import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.domain.valueObjects.BuyerAddress;
import com.nce.backend.users.infrastructure.jpa.entities.*;
import org.springframework.stereotype.Service;

@Service
public class UserJpaEntityMapper {

    public User toUserDomainEntity(UserJpaEntity jpaEntity) {
        if (jpaEntity instanceof SellerJpaEntity sellerEntity) {
            return toSellerUserDomainEntity(sellerEntity);
        } else if (jpaEntity instanceof BuyerJpaEntity buyerEntity) {
            return toBuyerUserDomainEntity(buyerEntity);
        } else if (jpaEntity instanceof OneTimeSellerJpaEntity oneTimeSellerEntity) {
            return toOneTimeSellerDomainEntity(oneTimeSellerEntity);
        } else if (jpaEntity instanceof  AdminJpaEntity adminEntity) {
            return toAdminDomainEntity(adminEntity);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + jpaEntity.getClass().getSimpleName());
        }
    }

    public UserJpaEntity toUserJpaEntity(User domainEntity) {
        if (domainEntity instanceof SellerUser sellerUser) {
            return toSellerJpaEntity(sellerUser);
        } else if (domainEntity instanceof BuyerUser buyerUser) {
            return toBuyerJpaEntity(buyerUser);
        } else if (domainEntity instanceof OneTimeSellerUser oneTimeSellerUser) {
            return toOneTimeSellerJpaEntity(oneTimeSellerUser);
        } else if (domainEntity instanceof  AdminUser adminUser) {
            return toAdminJpaEntity(adminUser);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + domainEntity.getClass().getSimpleName());
        }
    }

    private AdminUser toAdminDomainEntity(AdminJpaEntity jpaEntity) {
        return AdminUser
                .builder()
                .phoneNumber(jpaEntity.getPhoneNumber())
                .name(jpaEntity.getName())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .id(jpaEntity.getId())
                .role(jpaEntity.getRole())
                .isAccountLocked(jpaEntity.isAccountLocked())
                .fallbackEmail(jpaEntity.getFallbackEmail())
                .build();
    }

    private AdminJpaEntity toAdminJpaEntity(AdminUser domainEntity) {
       return AdminJpaEntity
                .builder()
                .phoneNumber(domainEntity.getPhoneNumber())
                .name(domainEntity.getName())
                .email(domainEntity.getEmail())
                .password(domainEntity.getPassword())
                .id(domainEntity.getId())
                .role(domainEntity.getRole())
                .isAccountLocked(domainEntity.isAccountLocked())
                .fallbackEmail(domainEntity.getFallbackEmail())
                .build();
    }

    private OneTimeSellerUser toOneTimeSellerDomainEntity(OneTimeSellerJpaEntity oneTimeSellerEntity) {
        return OneTimeSellerUser
                .builder()
                .id(oneTimeSellerEntity.getId())
                .phoneNumber(oneTimeSellerEntity.getPhoneNumber())
                .name(oneTimeSellerEntity.getName())
                .email(oneTimeSellerEntity.getEmail())
                .password(oneTimeSellerEntity.getPassword())
                .carId(oneTimeSellerEntity.getCarId())
                .role(oneTimeSellerEntity.getRole())
                .isAccountLocked(oneTimeSellerEntity.isAccountLocked())
                .build();
    }


    private OneTimeSellerJpaEntity toOneTimeSellerJpaEntity(OneTimeSellerUser oneTimeSellerUser) {
        return OneTimeSellerJpaEntity
                .builder()
                .id(oneTimeSellerUser.getId())
                .phoneNumber(oneTimeSellerUser.getPhoneNumber())
                .name(oneTimeSellerUser.getName())
                .email(oneTimeSellerUser.getEmail())
                .password(oneTimeSellerUser.getPassword())
                .carId(oneTimeSellerUser.getCarId())
                .role(oneTimeSellerUser.getRole())
                .isAccountLocked(oneTimeSellerUser.isAccountLocked())
                .build();
    }

    private SellerJpaEntity toSellerJpaEntity(SellerUser domainEntity) {
        return SellerJpaEntity
                .builder()
                .id(domainEntity.getId())
                .phoneNumber(domainEntity.getPhoneNumber())
                .name(domainEntity.getName())
                .email(domainEntity.getEmail())
                .password(domainEntity.getPassword())
                .role(domainEntity.getRole())
                .carIDs(domainEntity.getCarIDs())
                .sellerAddress(toAddressJpaEntity(domainEntity.getSellerAddress()))
                .isAccountLocked(domainEntity.isAccountLocked())
                .build();
    }

    public SellerUser toSellerUserDomainEntity(SellerJpaEntity jpaEntity) {
        return SellerUser
                .builder()
                .phoneNumber(jpaEntity.getPhoneNumber())
                .name(jpaEntity.getName())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .id(jpaEntity.getId())
                .role(jpaEntity.getRole())
                .carIDs(jpaEntity.getCarIDs())
                .sellerAddress(
                        Address
                                .builder()
                                .streetAddress(jpaEntity.getSellerAddress().getStreetAddress())
                                .postalLocation(jpaEntity.getSellerAddress().getPostalLocation())
                                .postalCode(jpaEntity.getSellerAddress().getPostalCode())
                                .build()
                )
                .isAccountLocked(jpaEntity.isAccountLocked())
                .build();
    }

    public BuyerUser toBuyerUserDomainEntity(BuyerJpaEntity jpaEntity) {
        return BuyerUser
                .builder()
                .phoneNumber(jpaEntity.getPhoneNumber())
                .name(jpaEntity.getName())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .id(jpaEntity.getId())
                .role(jpaEntity.getRole())
                .organisationAddress(
                        BuyerAddress
                                .builder()
                                .streetAddress(jpaEntity.getOrganisationAddress().getStreetAddress())
                                .postalLocation(jpaEntity.getOrganisationAddress().getPostalLocation())
                                .postalCode(jpaEntity.getOrganisationAddress().getPostalCode())
                                .country(jpaEntity.getOrganisationAddress().getCountry())
                                .build()
                )
                .organisationLicenceURLs(jpaEntity.getOrganisationLicenceURLs())
                .organisationNumber(jpaEntity.getOrganisationNumber())
                .organisationName(jpaEntity.getOrganisationName())
                .isAccountLocked(jpaEntity.isAccountLocked())
                .build();
    }

    private BuyerJpaEntity toBuyerJpaEntity(BuyerUser domainEntity) {
        return BuyerJpaEntity
                .builder()
                .id(domainEntity.getId())
                .phoneNumber(domainEntity.getPhoneNumber())
                .name(domainEntity.getName())
                .email(domainEntity.getEmail())
                .password(domainEntity.getPassword())
                .role(domainEntity.getRole())
                .organisationAddress(toBuyerAddressJpaEntity(domainEntity.getOrganisationAddress()))
                .organisationName(domainEntity.getOrganisationName())
                .organisationLicenceURLs(domainEntity.getOrganisationLicenceURLs())
                .organisationNumber(domainEntity.getOrganisationNumber())
                .isAccountLocked(domainEntity.isAccountLocked())
                .build();
    }

    private AddressJpaEntity toAddressJpaEntity(Address domainEntity) {
        return AddressJpaEntity
                .builder()
                .postalLocation(domainEntity.getPostalLocation())
                .postalCode(domainEntity.getPostalCode())
                .streetAddress(domainEntity.getStreetAddress())
                .build();
    }

    private BuyerAddressJpaEntity toBuyerAddressJpaEntity(BuyerAddress domainEntity) {
        return BuyerAddressJpaEntity
                .builder()
                .postalLocation(domainEntity.getPostalLocation())
                .postalCode(domainEntity.getPostalCode())
                .streetAddress(domainEntity.getStreetAddress())
                .country(domainEntity.getCountry())
                .build();
    }

}

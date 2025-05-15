package com.nce.backend.users.infrastructure.jpa;

import com.nce.backend.users.domain.entities.*;
import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.domain.valueObjects.BuyerCompanyAddress;
import com.nce.backend.users.infrastructure.jpa.entities.*;
import com.nce.backend.users.infrastructure.jpa.entities.buyer.BuyerCompanyAddressJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.buyer.BuyerCompanyJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.buyer.BuyerRepresentativeJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.seller.AddressJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.seller.OneTimeSellerJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.seller.SellerJpaEntity;
import org.springframework.stereotype.Service;

@Service
public class UserJpaEntityMapper {

    public User toUserDomainEntity(UserJpaEntity jpaEntity) {
        if (jpaEntity instanceof SellerJpaEntity sellerEntity) {
            return toSellerUserDomainEntity(sellerEntity);
        } else if (jpaEntity instanceof BuyerCompanyJpaEntity buyerEntity) {
            return toBuyerCompanyUserDomainEntity(buyerEntity);
        } else if (jpaEntity instanceof OneTimeSellerJpaEntity oneTimeSellerEntity) {
            return toOneTimeSellerDomainEntity(oneTimeSellerEntity);
        } else if (jpaEntity instanceof  AdminJpaEntity adminEntity) {
            return toAdminDomainEntity(adminEntity);
        } else if (jpaEntity instanceof  BuyerRepresentativeJpaEntity repEntity) {
            return toBuyerRepresentativeDomain(repEntity);
        } else {
            throw new IllegalArgumentException("Unsupported entity type: " + jpaEntity.getClass().getSimpleName());
        }
    }

    public UserJpaEntity toUserJpaEntity(User domainEntity) {
        if (domainEntity instanceof SellerUser sellerUser) {
            return toSellerJpaEntity(sellerUser);
        } else if (domainEntity instanceof BuyerCompanyUser buyerCompanyUser) {
            return toBuyerCompanyJpaEntity(buyerCompanyUser);
        } else if (domainEntity instanceof OneTimeSellerUser oneTimeSellerUser) {
            return toOneTimeSellerJpaEntity(oneTimeSellerUser);
        } else if (domainEntity instanceof  AdminUser adminUser) {
            return toAdminJpaEntity(adminUser);
        } else if (domainEntity instanceof  BuyerRepresentativeUser repUser) {
            return toBuyerRepresentativeJpaEntity(repUser);
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

    public BuyerCompanyUser toBuyerCompanyUserDomainEntity(BuyerCompanyJpaEntity jpaEntity) {
        return BuyerCompanyUser
                .builder()
                .phoneNumber(jpaEntity.getPhoneNumber())
                .name(jpaEntity.getName())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .id(jpaEntity.getId())
                .role(jpaEntity.getRole())
                .organisationAddress(
                        BuyerCompanyAddress
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
                .companyRepresentatives(
                        jpaEntity
                                .getRepresentatives()
                                .stream()
                                .map(this::toBuyerRepresentativeDomain)
                                .toList()
                )
                .isAccountLocked(jpaEntity.isAccountLocked())
                .build();
    }

    private BuyerCompanyJpaEntity toBuyerCompanyJpaEntity(BuyerCompanyUser domainEntity) {
        return BuyerCompanyJpaEntity
                .builder()
                .id(domainEntity.getId())
                .phoneNumber(domainEntity.getPhoneNumber())
                .name(domainEntity.getName())
                .email(domainEntity.getEmail())
                .password(domainEntity.getPassword())
                .role(domainEntity.getRole())
                .organisationAddress(toBuyerCompanyAddressJpaEntity(domainEntity.getOrganisationAddress()))
                .organisationName(domainEntity.getOrganisationName())
                .organisationLicenceURLs(domainEntity.getOrganisationLicenceURLs())
                .organisationNumber(domainEntity.getOrganisationNumber())
                .representatives(
                        domainEntity
                                .getCompanyRepresentatives()
                                .stream()
                                .map(this::toBuyerRepresentativeJpaEntity)
                                .toList()
                )
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

    private BuyerCompanyAddressJpaEntity toBuyerCompanyAddressJpaEntity(BuyerCompanyAddress domainEntity) {
        return BuyerCompanyAddressJpaEntity
                .builder()
                .postalLocation(domainEntity.getPostalLocation())
                .postalCode(domainEntity.getPostalCode())
                .streetAddress(domainEntity.getStreetAddress())
                .country(domainEntity.getCountry())
                .build();
    }

    private BuyerRepresentativeJpaEntity toBuyerRepresentativeJpaEntity(BuyerRepresentativeUser domainEntity){
        return BuyerRepresentativeJpaEntity
                .builder()
                .buyerCompanyId(domainEntity.getBuyerCompanyId())
                .savedCarIds(domainEntity.getSavedCarIds())
                .id(domainEntity.getId())
                .email(domainEntity.getEmail())
                .password(domainEntity.getPassword())
                .role(domainEntity.getRole())
                .name(domainEntity.getName())
                .phoneNumber(domainEntity.getPhoneNumber())
                .isAccountLocked(domainEntity.isAccountLocked())
                .build();
    }

    public BuyerRepresentativeUser toBuyerRepresentativeDomain(BuyerRepresentativeJpaEntity jpaEntity){
        return BuyerRepresentativeUser
                .builder()
                .buyerCompanyId(jpaEntity.getBuyerCompanyId())
                .savedCarIds(jpaEntity.getSavedCarIds())
                .id(jpaEntity.getId())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .role(jpaEntity.getRole())
                .name(jpaEntity.getName())
                .phoneNumber(jpaEntity.getPhoneNumber())
                .isAccountLocked(jpaEntity.isAccountLocked())
                .build();
    }

}

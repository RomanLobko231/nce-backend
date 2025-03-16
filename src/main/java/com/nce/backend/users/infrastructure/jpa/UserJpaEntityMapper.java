package com.nce.backend.users.infrastructure.jpa;

import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.SellerUserRepository;
import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.infrastructure.jpa.entities.AddressJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.SellerJpaEntity;
import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import org.springframework.stereotype.Service;

@Service
public class UserJpaEntityMapper {

    public User toUserDomainEntity(UserJpaEntity jpaEntity) {
        return SellerUser
                .builder()
                .phoneNumber(jpaEntity.getPhoneNumber())
                .name(jpaEntity.getName())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .id(jpaEntity.getId())
                .role(jpaEntity.getRole())
                .build();
    }

    public SellerJpaEntity toSellerJpaEntity(SellerUser domainEntity) {
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
                .build();
    }

    private AddressJpaEntity toAddressJpaEntity(Address domainEntity) {
        return new AddressJpaEntity(
                domainEntity.streetAddress(),
                domainEntity.postalLocation(),
                domainEntity.postalCode()
        );
    }

}

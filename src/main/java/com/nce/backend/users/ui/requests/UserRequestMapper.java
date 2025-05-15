package com.nce.backend.users.ui.requests;

import com.nce.backend.users.domain.entities.*;
import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.domain.valueObjects.BuyerCompanyAddress;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.ui.requests.address.ValidatedAddress;
import com.nce.backend.users.ui.requests.address.ValidatedBuyerCompanyAddress;
import com.nce.backend.users.ui.requests.register.RegisterBuyerCompanyRequest;
import com.nce.backend.users.ui.requests.register.RegisterOneTimeSellerRequest;
import com.nce.backend.users.ui.requests.register.RegisterRepresentativeRequest;
import com.nce.backend.users.ui.requests.register.RegisterSellerRequest;
import com.nce.backend.users.ui.requests.update.*;
import org.springframework.stereotype.Service;

@Service
public class UserRequestMapper {

    public User toDomainEntity(UpdateUserRequest updateUserRequest) {
        if (updateUserRequest instanceof UpdateSellerRequest request) {
            return toSellerUserFromUpdate(request);
        } else if (updateUserRequest instanceof UpdateBuyerCompanyRequest request) {
            return toBuyerCompanyUserFromUpdate(request);
        } else if (updateUserRequest instanceof UpdateOneTimeSellerRequest request) {
            return toOneTimeSellerFromUpdate(request);
        } else if (updateUserRequest instanceof UpdateBuyerRepresentativeRequest request) {
            return toRepresentativeFromUpdate(request);
        } else {
            throw new IllegalArgumentException("Unknown update request type");
        }
    }

    private BuyerRepresentativeUser toRepresentativeFromUpdate(UpdateBuyerRepresentativeRequest request) {
        return BuyerRepresentativeUser
                .builder()
                .buyerCompanyId(request.getBuyerCompanyId())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .id(request.getId())
                .name(request.getName())
                .savedCarIds(request.getSavedCarIds())
                .isAccountLocked(request.isAccountLocked())
                .role(Role.valueOf(request.getRole()))
                .build();
    }

    private SellerUser toSellerUserFromUpdate(UpdateSellerRequest request) {
        return SellerUser
                .builder()
                .role(Role.valueOf(request.getRole()))
                .isAccountLocked(request.isAccountLocked())
                .id(request.getId())
                .name(request.getName())
                .carIDs(request.getCarIDs())
                .email(request.getEmail())
                .sellerAddress(
                        toDomainAddress(request.getAddress())
                )
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    public BuyerRepresentativeUser toRepresentativeFromRegister(RegisterRepresentativeRequest request) {
        return BuyerRepresentativeUser
                .builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .buyerCompanyId(request.buyerCompanyId())
                .build();
    }

    private BuyerCompanyUser toBuyerCompanyUserFromUpdate(UpdateBuyerCompanyRequest request) {
        return BuyerCompanyUser
                .builder()
                .role(Role.valueOf(request.getRole()))
                .isAccountLocked(request.isAccountLocked())
                .id(request.getId())
                .name(request.getName())
                .organisationLicenceURLs(request.getOrganisationLicenceURLs())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .organisationName(request.getOrganisationName())
                .organisationNumber(request.getOrganisationNumber())
                .organisationAddress(
                        toDomainBuyerCompanyAddress(request.getAddress())
                )
                .companyRepresentatives(request.getRepresentatives())
                .build();
    }

    private OneTimeSellerUser toOneTimeSellerFromUpdate(UpdateOneTimeSellerRequest request) {
        return OneTimeSellerUser
                .builder()
                .role(Role.valueOf(request.getRole()))
                .isAccountLocked(request.isAccountLocked())
                .id(request.getId())
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .carId(request.getCarId())
                .email(request.getEmail())
                .build();
    }


    public SellerUser toSellerFromRegister(RegisterSellerRequest request){
        return SellerUser
                .builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .sellerAddress(
                        toDomainAddress(request.address())
                )
                .build();
    }

    public BuyerCompanyUser toBuyerFromRegister(RegisterBuyerCompanyRequest request){
        return BuyerCompanyUser
                .builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .organisationAddress(
                        toDomainBuyerCompanyAddress(request.organisationAddress())
                )
                .organisationName(request.organisationName())
                .organisationNumber(request.organisationNumber())
                .build();
    }

    public OneTimeSellerUser toOneTimeSellerFromRegister(RegisterOneTimeSellerRequest request) {
        return OneTimeSellerUser
                .builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .build();
    }

    private Address toDomainAddress(ValidatedAddress address) {
        return Address
                .builder()
                .streetAddress(address.streetAddress())
                .postalCode(address.postalCode())
                .postalLocation(address.postalLocation())
                .build();
    }

    private BuyerCompanyAddress toDomainBuyerCompanyAddress(ValidatedBuyerCompanyAddress address) {
        return BuyerCompanyAddress
                .builder()
                .streetAddress(address.streetAddress())
                .postalCode(address.postalCode())
                .postalLocation(address.postalLocation())
                .country(address.country())
                .build();
    }

}

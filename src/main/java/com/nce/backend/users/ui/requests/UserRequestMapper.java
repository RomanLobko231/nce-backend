package com.nce.backend.users.ui.requests;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.OneTimeSellerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.ui.requests.register.RegisterBuyerRequest;
import com.nce.backend.users.ui.requests.register.RegisterOneTimeSellerRequest;
import com.nce.backend.users.ui.requests.register.RegisterSellerRequest;
import com.nce.backend.users.ui.requests.update.UpdateBuyerRequest;
import com.nce.backend.users.ui.requests.update.UpdateOneTimeSellerRequest;
import com.nce.backend.users.ui.requests.update.UpdateSellerRequest;
import com.nce.backend.users.ui.requests.update.UpdateUserRequest;
import org.springframework.stereotype.Service;

@Service
public class UserRequestMapper {

    public User toDomainEntity(UpdateUserRequest updateUserRequest) {
        if (updateUserRequest instanceof UpdateSellerRequest request) {
            return toSellerUser(request);
        } else if (updateUserRequest instanceof UpdateBuyerRequest request) {
            return toBuyerUser(request);
        } else if (updateUserRequest instanceof UpdateOneTimeSellerRequest request) {
            return toOneTimeSeller(request);
        } else {
            throw new IllegalArgumentException("Unknown update request type");
        }
    }

    private static SellerUser toSellerUser(UpdateSellerRequest request) {
        return SellerUser
                .builder()
                .role(Role.valueOf(request.getRole()))
                .isAccountLocked(request.isAccountLocked())
                .id(request.getId())
                .name(request.getName())
                .carIDs(request.getCarIDs())
                .email(request.getEmail())
                .sellerAddress(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    private static BuyerUser toBuyerUser(UpdateBuyerRequest request) {
        return BuyerUser
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
                .organisationAddress(request.getAddress())
                .build();
    }

    private static OneTimeSellerUser toOneTimeSeller(UpdateOneTimeSellerRequest request) {
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


    public SellerUser toSellerFromRegisterRequest(RegisterSellerRequest request){
        return SellerUser
                .builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .sellerAddress(request.address())
                .build();
    }

    public BuyerUser toBuyerFromRegisterRequest(RegisterBuyerRequest request){
        return BuyerUser
                .builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .organisationAddress(request.organisationAddress())
                .organisationName(request.organisationName())
                .organisationNumber(request.organisationNumber())
                .build();
    }

    public OneTimeSellerUser toOneTimeSellerUser( RegisterOneTimeSellerRequest request) {
        return OneTimeSellerUser
                .builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .build();
    }
}

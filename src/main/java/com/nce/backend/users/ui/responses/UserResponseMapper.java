package com.nce.backend.users.ui.responses;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserResponseMapper {

    public RegisterSuccessResponse toRegisterSuccessResponse(User registeredUser) {
        return RegisterSuccessResponse
                .builder()
                .userId(registeredUser.getId())
                .name(registeredUser.getName())
                .build();
    }

    public UserResponse toSellerUserResponse(SellerUser user) {
        System.out.println(user.getSellerAddress().streetAddress());
        return SellerUserResponse
                .builder()
                .address(user.getSellerAddress())
                .carIds(user.getCarIDs())
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole().name())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .isAccountLocked(user.isAccountLocked())
                .build();
    }

    public UserResponse toBuyerUserResponse(BuyerUser user) {
        return BuyerUserResponse
                .builder()
                .organisationAddress(user.getOrganisationAddress())
                .organisationName(user.getOrganisationName())
                .organisationNumber(user.getOrganisationNumber())
                .organisationLicenceURLs(user.getOrganisationLicenceURLs())
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole().name())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .isAccountLocked(user.isAccountLocked())
                .build();
    }
}

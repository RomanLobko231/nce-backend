package com.nce.backend.users.ui.responses;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.OneTimeSellerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.ui.responses.userData.BuyerUserResponse;
import com.nce.backend.users.ui.responses.userData.OneTimeSellerUserResponse;
import com.nce.backend.users.ui.responses.userData.SellerUserResponse;
import com.nce.backend.users.ui.responses.userData.UserResponse;
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

    public UserResponse toUserResponse(User user) {
        if (user instanceof SellerUser sellerUser) {
           return toSellerUserResponse(sellerUser);
        } else if (user instanceof BuyerUser buyerUser) {
            return toBuyerUserResponse(buyerUser);
        } else if (user instanceof OneTimeSellerUser oneTimeSellerUser) {
            return toOneTimeSellerResponse(oneTimeSellerUser);
        } else {
            throw new IllegalStateException("Unknown user type");
        }
    }

    private OneTimeSellerUserResponse toOneTimeSellerResponse(OneTimeSellerUser oneTimeSellerUser) {
        return OneTimeSellerUserResponse
                .builder()
                .id(oneTimeSellerUser.getId())
                .name(oneTimeSellerUser.getName())
                .phoneNumber(oneTimeSellerUser.getPhoneNumber())
                .role(oneTimeSellerUser.getRole().name())
                .carId(oneTimeSellerUser.getCarId())
                .email(oneTimeSellerUser.getEmail())
                .isAccountLocked(oneTimeSellerUser.isAccountLocked())
                .build();
    }

    public SellerUserResponse toSellerUserResponse(SellerUser user) {
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

    public BuyerUserResponse toBuyerUserResponse(BuyerUser user) {
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

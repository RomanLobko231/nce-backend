package com.nce.backend.users.ui.responses;

import com.nce.backend.users.domain.entities.*;
import com.nce.backend.users.ui.responses.userData.*;
import com.nce.backend.users.ui.responses.userData.admin.AdminUserResponse;
import com.nce.backend.users.ui.responses.userData.buyer.BuyerCompanyUserBasicInfo;
import com.nce.backend.users.ui.responses.userData.buyer.BuyerCompanyUserResponse;
import com.nce.backend.users.ui.responses.userData.representative.RepresentativeUserResponse;
import com.nce.backend.users.ui.responses.userData.representative.RepresentativeWithCompanyResponse;
import com.nce.backend.users.ui.responses.userData.seller.OneTimeSellerUserResponse;
import com.nce.backend.users.ui.responses.userData.seller.SellerUserResponse;
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
        } else if (user instanceof BuyerCompanyUser buyerCompanyUser) {
            return toBuyerCompanyUserResponse(buyerCompanyUser);
        } else if (user instanceof OneTimeSellerUser oneTimeSellerUser) {
            return toOneTimeSellerResponse(oneTimeSellerUser);
        } else if (user instanceof BuyerRepresentativeUser representativeUser) {
            return toRepresentativeUserResponse(representativeUser);
        } else if (user instanceof AdminUser adminUser) {
            return toAdminUserResponse(adminUser);
        }else {
            throw new IllegalStateException("Unknown user type");
        }
    }

    private RepresentativeUserResponse toRepresentativeUserResponse(BuyerRepresentativeUser representativeUser) {
        return RepresentativeUserResponse
                .builder()
                .id(representativeUser.getId())
                .name(representativeUser.getName())
                .isAccountLocked(representativeUser.isAccountLocked())
                .email(representativeUser.getEmail())
                .phoneNumber(representativeUser.getPhoneNumber())
                .role(representativeUser.getRole().name())
                .savedCarIds(representativeUser.getSavedCarIds())
                .buyerCompanyId(representativeUser.getBuyerCompanyId())
                .build();
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

    public AdminUserResponse toAdminUserResponse(AdminUser user){
        return AdminUserResponse
                .builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole().name())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .isAccountLocked(user.isAccountLocked())
                .fallbackEmail(user.getFallbackEmail())
                .build();
    }

    public BuyerCompanyUserResponse toBuyerCompanyUserResponse(BuyerCompanyUser user) {
        return BuyerCompanyUserResponse
                .builder()
                .address(user.getOrganisationAddress())
                .organisationName(user.getOrganisationName())
                .organisationNumber(user.getOrganisationNumber())
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole().name())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .representatives(
                        user.getCompanyRepresentatives()
                                .stream()
                                .map(this::toRepresentativeUserResponse)
                                .toList()
                )
                .isAccountLocked(user.isAccountLocked())
                .build();
    }

    public RepresentativeWithCompanyResponse toRepresentativeWithCompanyResponse(
            BuyerRepresentativeUser representative,
            BuyerCompanyUser company
    ) {
        return RepresentativeWithCompanyResponse
                .builder()
                .representative(
                        this.toRepresentativeUserResponse(representative)
                )
                .companyEmail(company.getEmail())
                .companyName(company.getOrganisationName())
                .companyPhoneNumber(company.getPhoneNumber())
                .companyContactPerson(company.getName())
                .build();
    }

    public BuyerCompanyUserBasicInfo toBuyerCompanyUserBasicInfo(BuyerCompanyUser user) {
        return BuyerCompanyUserBasicInfo
                .builder()
                .address(user.getOrganisationAddress())
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

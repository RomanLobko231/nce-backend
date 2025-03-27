package com.nce.backend.users.ui.requests;

import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.OneTimeSellerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.valueObjects.Role;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserRequestMapper {

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

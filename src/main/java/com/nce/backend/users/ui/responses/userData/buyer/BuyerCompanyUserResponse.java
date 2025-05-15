package com.nce.backend.users.ui.responses.userData.buyer;

import com.nce.backend.users.domain.valueObjects.BuyerCompanyAddress;
import com.nce.backend.users.ui.responses.userData.UserResponse;
import com.nce.backend.users.ui.responses.userData.representative.RepresentativeUserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class BuyerCompanyUserResponse extends UserResponse {

    private String organisationName;

    private String organisationNumber;

    private BuyerCompanyAddress address;

    private List<RepresentativeUserResponse> representatives;
}

package com.nce.backend.users.ui.responses.userData.buyer;

import com.nce.backend.users.domain.valueObjects.BuyerCompanyAddress;
import com.nce.backend.users.ui.responses.userData.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class BuyerCompanyUserBasicInfo extends UserResponse {
    private String organisationName;

    private String organisationNumber;

    private List<String> organisationLicenceURLs;

    private BuyerCompanyAddress address;
}

package com.nce.backend.users.ui.responses.userData;

import com.nce.backend.users.domain.valueObjects.BuyerAddress;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@Setter
public class BuyerUserResponse extends UserResponse {

    private String organisationName;

    private String organisationNumber;

    private List<String> organisationLicenceURLs;

    private BuyerAddress organisationAddress;
}

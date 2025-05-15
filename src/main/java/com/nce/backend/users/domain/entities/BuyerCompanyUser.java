package com.nce.backend.users.domain.entities;

import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.domain.valueObjects.BuyerCompanyAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class BuyerCompanyUser extends User {

     private String organisationName;

     private String organisationNumber;

     private List<String> organisationLicenceURLs;

     private BuyerCompanyAddress organisationAddress;

     @Builder.Default
     private List<BuyerRepresentativeUser> companyRepresentatives = new ArrayList<>();

}

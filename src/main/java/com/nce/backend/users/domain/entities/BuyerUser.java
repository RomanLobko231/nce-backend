package com.nce.backend.users.domain.entities;

import com.nce.backend.users.domain.valueObjects.Address;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class BuyerUser extends User {

     private String organisationName;

     private String organisationNumber;

     private List<String> organisationLicenceURLs;

     private Address organisationAddress;
}

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

     @Override
     protected void updateFieldsFrom(User source) {
          BuyerCompanyUser other = (BuyerCompanyUser) source;

          if(other.getName() != null) this.setName(other.getName());
          if(other.getPhoneNumber() != null) this.setPhoneNumber(other.getPhoneNumber());
          if(other.getOrganisationAddress() != null) this.setOrganisationAddress(other.getOrganisationAddress());
     }

     @Override
     public void setAccountLock(boolean isAccountLocked) {
          this.setAccountLocked(isAccountLocked);
          companyRepresentatives.forEach(r -> r.setAccountLocked(isAccountLocked));
     }
}

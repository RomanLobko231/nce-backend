package com.nce.backend.users.ui.responses.userData.representative;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class RepresentativeWithCompanyResponse {

    private RepresentativeUserResponse representative;

    private String companyName;

    private String companyEmail;

    private String companyPhoneNumber;

    private String companyContactPerson;

}

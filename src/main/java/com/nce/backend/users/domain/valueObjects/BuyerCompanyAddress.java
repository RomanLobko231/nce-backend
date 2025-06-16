package com.nce.backend.users.domain.valueObjects;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyerCompanyAddress extends Address {

    private String country;
}

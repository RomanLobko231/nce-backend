package com.nce.backend.users.domain.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerRepresentativeUser extends User {

    @Builder.Default
    private List<UUID> savedCarIds = new ArrayList<>();

    private UUID buyerCompanyId;

}

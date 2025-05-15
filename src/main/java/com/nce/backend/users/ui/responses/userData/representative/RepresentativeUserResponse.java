package com.nce.backend.users.ui.responses.userData.representative;

import com.nce.backend.users.ui.responses.userData.UserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@Getter
@Setter
public class RepresentativeUserResponse extends UserResponse {

    private List<UUID> savedCarIds;

    private UUID buyerCompanyId;
}

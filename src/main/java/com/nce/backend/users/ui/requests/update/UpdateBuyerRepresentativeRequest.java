package com.nce.backend.users.ui.requests.update;


import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.persistence.Column;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@JsonTypeName("BUYER_REPRESENTATIVE")
public class UpdateBuyerRepresentativeRequest extends UpdateUserRequest {

    private List<UUID> savedCarIds;

    private UUID buyerCompanyId;
}

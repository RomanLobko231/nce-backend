package com.nce.backend.users.ui.requests.update;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@Getter
@JsonTypeName("ADMIN")
public class UpdateAdminRequest extends UpdateUserRequest{
    private String fallbackEmail;
}

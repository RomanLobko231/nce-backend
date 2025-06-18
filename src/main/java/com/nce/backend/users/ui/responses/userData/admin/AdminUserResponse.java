package com.nce.backend.users.ui.responses.userData.admin;

import com.nce.backend.users.ui.responses.userData.UserResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class AdminUserResponse extends UserResponse {

    String fallbackEmail;
}

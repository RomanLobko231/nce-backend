package com.nce.backend.users.ui.requests.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nce.backend.users.domain.valueObjects.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        property = "role")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateSellerRequest.class, name = "SELLER"),
        @JsonSubTypes.Type(value = UpdateOneTimeSellerRequest.class, name = "ONE_TIME_SELLER"),
        @JsonSubTypes.Type(value = UpdateBuyerCompanyRequest.class, name = "BUYER_COMPANY"),
        @JsonSubTypes.Type(value = UpdateBuyerRepresentativeRequest.class, name = "BUYER_REPRESENTATIVE"),
        @JsonSubTypes.Type(value = UpdateAdminRequest.class, name = "SUPER_ADMIN"),
        @JsonSubTypes.Type(value = UpdateAdminRequest.class, name = "ADMIN"),
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotNull(message = "Id cannot be null")
    private UUID id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    @NotNull(message = "Role cannot be null")
    @NotBlank(message = "Role cannot be blank")
    @JsonProperty(value = "role")
    private String role;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotNull(message = "isAccountLocked cannot be null")
    @JsonProperty(value = "accountLocked")
    private boolean isAccountLocked;
}

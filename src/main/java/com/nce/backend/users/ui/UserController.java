package com.nce.backend.users.ui;

import com.nce.backend.users.domain.entities.BuyerCompanyUser;
import com.nce.backend.users.domain.entities.BuyerRepresentativeUser;
import com.nce.backend.users.ui.requests.register.*;
import com.nce.backend.users.ui.requests.update.PasswordUpdateRequest;
import com.nce.backend.users.ui.requests.update.UpdateUserRequest;
import com.nce.backend.users.application.service.UserApplicationService;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.ui.requests.*;
import com.nce.backend.users.ui.responses.*;
import com.nce.backend.users.ui.responses.userData.*;
import com.nce.backend.users.ui.responses.userData.admin.AdminUserResponse;
import com.nce.backend.users.ui.responses.userData.buyer.BuyerCompanyUserBasicInfo;
import com.nce.backend.users.ui.responses.userData.representative.RepresentativeWithCompanyResponse;
import com.nce.backend.users.ui.responses.userData.seller.SellerUserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@EnableMethodSecurity
public class UserController {

    private final UserApplicationService userService;
    private final UserRequestMapper requestMapper;
    private final UserResponseMapper responseMapper;


    @PostMapping(value = "/register-seller")
    ResponseEntity<RegisterSuccessResponse> registerSeller(@RequestBody @Valid RegisterSellerRequest request) {
        User registeredUser = userService.registerSeller(
                requestMapper.toSellerFromRegister(request)
        );

        return ResponseEntity.ok(responseMapper.toRegisterSuccessResponse(registeredUser));
    }

    @PostMapping(value = "/register-one-time-seller")
    ResponseEntity<RegisterSuccessResponse> registerOneTimeSeller(@RequestBody @Valid RegisterOneTimeSellerRequest request) {
        User registeredUser = userService.registerOneTimeSeller(
                requestMapper.toOneTimeSellerFromRegister(request)
        );

        return ResponseEntity.ok(responseMapper.toRegisterSuccessResponse(registeredUser));
    }

    @PostMapping(value = "/register-buyer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<RegisterSuccessResponse> registerBuyerCompany(
            @RequestPart(name = "buyerData") @Valid RegisterBuyerCompanyRequest request,
            @RequestPart(name = "organisationLicences") List<MultipartFile> organisationLicences
    ) {
        User registeredUser = userService.registerBuyerCompany(
                requestMapper.toBuyerFromRegister(request),
                organisationLicences
        );

        return ResponseEntity.ok(responseMapper.toRegisterSuccessResponse(registeredUser));
    }

    @PostMapping(value = "/register-representative")
    @PreAuthorize("authentication.principal.isAccountLocked == false")
    ResponseEntity<UserResponse> registerBuyerRepresentative(
            @RequestBody @Valid RegisterRepresentativeRequest request
    ) {
        User registeredUser = userService.registerRepresentative(
                requestMapper.toRepresentativeFromRegister(request)
        );

        return ResponseEntity.ok(responseMapper.toUserResponse(registeredUser));
    }

    @PostMapping(value = "/register-admin")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    ResponseEntity<UserResponse> registerAdmin(
            @RequestBody @Valid RegisterAdminRequest request
    ) {
        User newAdmin = userService.registerAdmin(
                requestMapper.toAdminFromRegister(request)
        );

        return ResponseEntity.ok(responseMapper.toUserResponse(newAdmin));
    }

    @PostMapping("/login")
    ResponseEntity<AuthSuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthSuccessResponse authResponse = userService.authenticateUser(loginRequest);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<UserResponse> getUserById(@PathVariable(name = "id") UUID id) {
        User user = userService.findUserById(id);
        UserResponse response = responseMapper.toUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-email/{email}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<UserResponse> getUserByEmail(@PathVariable(name = "email") String email) {
        User user = userService.findUserByEmail(email);
        UserResponse response = responseMapper.toUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-number/{phoneNumber}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<UserResponse> getUserByPhoneNumber(@PathVariable(name = "phoneNumber") String number) {
        User user = userService.findUserByPhoneNumber(number);
        UserResponse response = responseMapper.toUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping()
    @PreAuthorize(
            "hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN') or " +
                    "#request.id == authentication.principal.id or " +
                    "@userDomainService.companyHasRepresentativeById(authentication.principal.id, #request.id)")
    ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserRequest request) {
        User savedUser = userService.updateUser(
                requestMapper.toDomainEntityFromUpdate(request)
        );
        UserResponse response = responseMapper.toUserResponse(savedUser);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update-password")
    @PreAuthorize(
            "hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN') or " +
                    "#id == authentication.principal.id or " +
                    "@userDomainService.companyHasRepresentativeById(authentication.principal.id, #id)")
    ResponseEntity<Void> updateUserPassword(
            @PathVariable(name = "id") @NotNull UUID id,
            @RequestBody @Valid PasswordUpdateRequest request
    ) {
        userService.updatePasswordById(id, request.password());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/as-admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<UserResponse> updateUserAsAdmin(@RequestBody @Valid UpdateUserRequest request) {
        User savedUser = userService.updateUserAsAdmin(
                requestMapper.toDomainEntityFromUpdate(request)
        );
        UserResponse response = responseMapper.toUserResponse(savedUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(
                userService
                        .findAllUsers()
                        .stream()
                        .map(responseMapper::toUserResponse)
                        .toList()
        );
    }

    @GetMapping("/sellers")
    ResponseEntity<List<SellerUserResponse>> getAllSellers() {
        return ResponseEntity.ok(
                userService
                        .findAllSellers()
                        .stream()
                        .map(responseMapper::toSellerUserResponse)
                        .toList()
        );
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<List<AdminUserResponse>> getAllAdmins() {
        return ResponseEntity.ok(
                userService
                        .findAllAdmins()
                        .stream()
                        .map(responseMapper::toAdminUserResponse)
                        .toList()
        );
    }

    @GetMapping("/buyers")
    ResponseEntity<List<BuyerCompanyUserBasicInfo>> getAllBuyers(
            @RequestParam(name = "isLocked", required = false) Boolean isLocked) {

        List<BuyerCompanyUser> buyers = (isLocked == null)
                ? userService.findAllBuyerCompanies()
                : userService.findAllBuyerCompaniesByLocked(isLocked);

        return ResponseEntity.ok(
                buyers
                        .stream()
                        .map(responseMapper::toBuyerCompanyUserBasicInfo)
                        .toList()
        );
    }

    @GetMapping("/buyers/{id}/licences")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<List<String>> getLicenceUrlsByBuyerId(@PathVariable UUID id) {
        List<String> urls = userService.getLicenceUrlsByBuyerId(id);

        return ResponseEntity.ok(urls);
    }

    @GetMapping("/representatives/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<UserResponse> getRepresentativeById(@PathVariable UUID id) {
        BuyerRepresentativeUser representative = userService.getRepresentativeById(id);
        UserResponse response = responseMapper.toUserResponse(representative);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/representatives/{id}/with-company")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<RepresentativeWithCompanyResponse> getRepresentativeWithCompanyById(@PathVariable UUID id) {
        BuyerRepresentativeUser representative = userService.getRepresentativeById(id);
        BuyerCompanyUser company = userService.getCompanyById(representative.getBuyerCompanyId());

        RepresentativeWithCompanyResponse response = responseMapper
                .toRepresentativeWithCompanyResponse(representative, company);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN') or " +
                    "#id == authentication.principal.id or " +
                    "@userDomainService.companyHasRepresentativeById(authentication.principal.id, #id)"
    )
    ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/set-lock")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<Void> setLockOnUserAccount(
            @PathVariable UUID id,
            @RequestParam(name = "isLocked") boolean isLocked
    ) {
        userService.setIsAccountLocked(id, isLocked);

        return ResponseEntity.noContent().build();
    }


}

package com.nce.backend.users.ui;

import com.nce.backend.users.application.UserApplicationService;
import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import com.nce.backend.users.infrastructure.jpa.repositories.UserJpaRepository;
import com.nce.backend.users.ui.requests.*;
import com.nce.backend.users.ui.responses.*;
import jakarta.validation.Valid;
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


    @PostMapping(value = "/register_seller")
    ResponseEntity<RegisterSuccessResponse> registerSeller(@RequestBody @Valid RegisterSellerRequest request) {
        User registeredUser = userService.registerSeller(
                requestMapper.toSellerFromRegisterRequest(request)
        );

        return ResponseEntity.ok(responseMapper.toRegisterSuccessResponse(registeredUser));
    }

    @PostMapping(value = "/register_one_time_seller")
    ResponseEntity<RegisterSuccessResponse> registerOneTimeSeller(@RequestBody @Valid RegisterOneTimeSellerRequest request) {
        User registeredUser = userService.registerOneTimeSeller(
                requestMapper.toOneTimeSellerUser(request)
        );

        return ResponseEntity.ok(responseMapper.toRegisterSuccessResponse(registeredUser));
    }

    @PostMapping(value = "/register_buyer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<RegisterSuccessResponse> registerBuyer(
            @RequestPart(name = "buyerData") @Valid RegisterBuyerRequest request,
            @RequestPart(name = "organisationLicences") List<MultipartFile> organisationLicences
    ) {
        User registeredUser = userService.registerBuyer(
                requestMapper.toBuyerFromRegisterRequest(request),
                organisationLicences
        );

        return ResponseEntity.ok(responseMapper.toRegisterSuccessResponse(registeredUser));
    }

    @PostMapping("/login")
    ResponseEntity<AuthSuccessResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthSuccessResponse authResponse = userService.authenticateUser(loginRequest);

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        UserResponse response = responseMapper.toUserResponse(user);

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

    @GetMapping("/buyers")
    ResponseEntity<List<BuyerUserResponse>> getAllBuyers() {
        return ResponseEntity.ok(
                userService
                        .findAllBuyers()
                        .stream()
                        .map(responseMapper::toBuyerUserResponse)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    ResponseEntity<Void> deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/set_lock")
    ResponseEntity<Void> unlockUserAccount(
            @PathVariable UUID id,
            @RequestParam(name = "isLocked") boolean isLocked
    ) {
        userService.setIsAccountLocked(id, isLocked);

        return ResponseEntity.noContent().build();
    }
}

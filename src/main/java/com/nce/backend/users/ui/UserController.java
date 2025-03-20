package com.nce.backend.users.ui;

import com.nce.backend.users.application.UserApplicationService;
import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.ui.requests.LoginRequest;
import com.nce.backend.users.ui.requests.RegisterBuyerRequest;
import com.nce.backend.users.ui.requests.RegisterSellerRequest;
import com.nce.backend.users.ui.requests.UserRequestMapper;
import com.nce.backend.users.ui.responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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
    ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        UserResponse response;

        if (user instanceof SellerUser sellerUser) {
            System.out.println(sellerUser.getSellerAddress().streetAddress());
            response = responseMapper.toSellerUserResponse(sellerUser);
        } else if (user instanceof BuyerUser buyerUser) {
            response = responseMapper.toBuyerUserResponse(buyerUser);
        } else {
            throw new IllegalStateException("Unknown user type");
        }

        return ResponseEntity.ok(response);
    }
}

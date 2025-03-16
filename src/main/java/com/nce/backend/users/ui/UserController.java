package com.nce.backend.users.ui;

import com.nce.backend.users.application.UserApplicationService;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.ui.requests.LoginRequest;
import com.nce.backend.users.ui.requests.RegisterBuyerRequest;
import com.nce.backend.users.ui.requests.RegisterSellerRequest;
import com.nce.backend.users.ui.requests.UserRequestMapper;
import com.nce.backend.users.ui.responses.AuthSuccessResponse;
import com.nce.backend.users.ui.responses.RegisterSuccessResponse;
import com.nce.backend.users.ui.responses.UserResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        User registeredUser = userService.registerSeller(requestMapper.toSellerFromRequest(request));

        return ResponseEntity.ok(responseMapper.toRegisterSuccessResponse(registeredUser));
    }

    @PostMapping(value = "/register_buyer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<AuthSuccessResponse> registerBuyer(
            @RequestPart(name = "request") @Valid RegisterBuyerRequest request,
            @RequestPart(name = "orgLicences") List<MultipartFile> organisationLicences
    ) {
        User registeredUser = userService.registerBuyer(
                requestMapper.toBuyerFromRequest(request),
                organisationLicences
        );

        return ResponseEntity.ok(AuthSuccessResponse.builder().build());
    }

    @GetMapping("/login")
    ResponseEntity<AuthSuccessResponse> login(@RequestBody LoginRequest request){
        AuthSuccessResponse authResponse = userService.authenticateUser(request);

        return ResponseEntity.ok(authResponse);
    }
}

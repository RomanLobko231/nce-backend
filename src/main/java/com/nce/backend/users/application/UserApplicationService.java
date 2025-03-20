package com.nce.backend.users.application;

import com.nce.backend.common.events.NewCarSavedEvent;
import com.nce.backend.file_storage.FileStorageFacade;
import com.nce.backend.security.SecurityFacade;
import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.services.UserDomainService;
import com.nce.backend.users.exceptions.UserAlreadyExistsException;
import com.nce.backend.users.exceptions.UserDoesNotExistException;
import com.nce.backend.users.ui.requests.LoginRequest;
import com.nce.backend.users.ui.responses.AuthSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final SecurityFacade securityFacade;
    private final UserDomainService userDomainService;
    private final FileStorageFacade fileStorageFacade;

    public SellerUser registerSeller(SellerUser userToRegister) {
        if (userDomainService.existsByEmail(userToRegister.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        this.setEncodedPassword(userToRegister);

        return userDomainService.registerSeller(userToRegister);
    }

    public BuyerUser registerBuyer(BuyerUser userToRegister, List<MultipartFile> organisationLicences) {
        if (userDomainService.existsByEmail(userToRegister.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        this.setEncodedPassword(userToRegister);
        if (organisationLicences != null && !organisationLicences.isEmpty()) {
            List<String> filesUrls = fileStorageFacade.uploadFiles(organisationLicences);
            userToRegister.setOrganisationLicenceURLs(filesUrls);
        }

        return userDomainService.registerBuyer(userToRegister);
    }

    public AuthSuccessResponse authenticateUser(LoginRequest request) {
        User user = userDomainService
                .findUserByEmail(request.email())
                .orElseThrow(
                        () -> new NoSuchElementException("User with email %s was not found".formatted(request.email()))
                );

        if (!securityFacade.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = securityFacade.generateToken(user.getEmail());

        return AuthSuccessResponse
                .builder()
                .token(token)
                .userId(user.getId())
                .build();
    }

    @Async
    @TransactionalEventListener
    public void addCarToUserOn(NewCarSavedEvent event) {
        SellerUser user = userDomainService
                .findSellerById(event.ownerId())
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(event.ownerId()))
                );

        user.addCarId(event.carId());

        userDomainService.updateUser(user);
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = securityFacade.encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public User findUserById(UUID id) {
        return userDomainService
                .findUserById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(id))
                );
    }
}

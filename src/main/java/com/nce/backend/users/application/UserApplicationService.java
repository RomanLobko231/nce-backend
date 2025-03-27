package com.nce.backend.users.application;

import com.nce.backend.common.events.NewCarSavedEvent;
import com.nce.backend.file_storage.FileStorageFacade;
import com.nce.backend.security.SecurityFacade;
import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.OneTimeSellerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.services.UserDomainService;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.exceptions.UserAlreadyExistsException;
import com.nce.backend.users.exceptions.UserDoesNotExistException;
import com.nce.backend.users.infrastructure.jpa.entities.UserJpaEntity;
import com.nce.backend.users.ui.requests.LoginRequest;
import com.nce.backend.users.ui.responses.AuthSuccessResponse;
import com.nce.backend.users.ui.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final SecurityFacade securityFacade;
    private final UserDomainService userDomainService;
    private final FileStorageFacade fileStorageFacade;

    public User registerSeller(SellerUser userToRegister) {
        if (userDomainService.existsByEmail(userToRegister.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        this.setEncodedPassword(userToRegister);

        return userDomainService.registerSeller(userToRegister);
    }

    public User registerBuyer(BuyerUser userToRegister, List<MultipartFile> organisationLicences) {
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

    public User registerOneTimeSeller(OneTimeSellerUser userToRegister) {
        String randomEmail = UUID.randomUUID().toString();
        String randomPassword = securityFacade.encode(UUID.randomUUID().toString());

        userToRegister.setEmail(randomEmail);
        userToRegister.setPassword(randomPassword);

        return userDomainService.registerOneTimeSeller(userToRegister);
    }

    public AuthSuccessResponse authenticateUser(LoginRequest request) {
        User user = userDomainService
                .findUserByEmail(request.email())
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with email %s was not found".formatted(request.email()))
                );

        if (!securityFacade.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials. Please check email or password.");
        }

        String token = securityFacade.generateToken(user.getEmail());

        return AuthSuccessResponse
                .builder()
                .token(token)
                .userId(user.getId())
                .build();
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = securityFacade.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public User findUserById(UUID id) {
        return userDomainService
                .findUserById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(id))
                );
    }

    public List<User> findAllUsers() {
        return userDomainService.findAllUsers();
    }

    public List<SellerUser> findAllSellers() {
        return userDomainService.findAllSellers();
    }

    public List<BuyerUser> findAllBuyers() {
        return userDomainService.findAllBuyers();
    }

    public void deleteUserById(UUID id) {
        userDomainService.deleteUserById(id);
    }

    @Async
    @TransactionalEventListener
    public void addCarToUserOn(NewCarSavedEvent event) {
        User userToUpdate = userDomainService
                .findUserById(event.ownerId())
                .filter(u -> u.getRole() == Role.SELLER || u.getRole() == Role.ONE_TIME_SELLER)
                .map(u -> addCarIdToUser(u, event.carId()))
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(event.ownerId()))
                );

        userDomainService.updateUser(userToUpdate);
    }

    private User addCarIdToUser(User user, UUID carId) {
        if (user instanceof OneTimeSellerUser oneTimeSellerUser) {
            oneTimeSellerUser.setCarId(carId);
            return oneTimeSellerUser;
        } else if (user instanceof SellerUser sellerUser) {
            sellerUser.addCarId(carId);
            return sellerUser;
        }
        throw new IllegalStateException("Wrong user type");
    }
}

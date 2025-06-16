package com.nce.backend.users.application.service;

import com.nce.backend.common.annotation.logging.LoggableAction;
import com.nce.backend.common.event.auction.NewBidPlacedEvent;
import com.nce.backend.common.event.car.CarDeletedEvent;
import com.nce.backend.common.event.car.CarSaveFailedRollbackUserEvent;
import com.nce.backend.common.event.car.NewCarSavedEvent;
import com.nce.backend.filestorage.FileStorageFacade;
import com.nce.backend.users.application.port.Authenticator;
import com.nce.backend.users.domain.entities.*;
import com.nce.backend.users.domain.services.UserDomainService;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.exceptions.UserAlreadyExistsException;
import com.nce.backend.users.exceptions.UserDoesNotExistException;
import com.nce.backend.users.ui.requests.LoginRequest;
import com.nce.backend.users.ui.responses.AuthSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final Authenticator authenticator;
    private final UserDomainService userDomainService;
    private final FileStorageFacade fileStorageFacade;

    @LoggableAction(action = "REGISTER_USER")
    public User registerSeller(SellerUser seller) {
        if (userDomainService.existsByEmail(seller.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        this.setEncodedPassword(seller);

        return userDomainService.registerSeller(seller);
    }

    @LoggableAction(action = "REGISTER_USER")
    public User registerBuyerCompany(BuyerCompanyUser buyerCompany, List<MultipartFile> organisationLicences) {
        if (userDomainService.existsByEmail(buyerCompany.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        this.setEncodedPassword(buyerCompany);

        List<String> filesUrls = fileStorageFacade.uploadFiles(organisationLicences);
        buyerCompany.setOrganisationLicenceURLs(filesUrls);

        return userDomainService.registerBuyerCompany(buyerCompany);
    }

    @LoggableAction(action = "REGISTER_USER")
    public User registerRepresentative(BuyerRepresentativeUser representative) {
        if (userDomainService.existsByEmail(representative.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        this.setEncodedPassword(representative);

        return userDomainService.registerRepresentative(representative);
    }

    @LoggableAction(action = "REGISTER_USER")
    public User registerOneTimeSeller(OneTimeSellerUser oneTimeSeller) {
        String randomEmail = UUID.randomUUID().toString();
        String randomPassword = authenticator.generateHash(UUID.randomUUID().toString());

        oneTimeSeller.setEmail(randomEmail);
        oneTimeSeller.setPassword(randomPassword);

        return userDomainService.registerOneTimeSeller(oneTimeSeller);
    }

    @LoggableAction(action = "USER_AUTH")
    public AuthSuccessResponse authenticateUser(LoginRequest request) {
        User user = userDomainService
                .findUserByEmail(request.email())
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with email %s was not found".formatted(request.email()))
                );

        if (!authenticator.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials. Please check email or password.");
        }

        String token = authenticator.generateTokenFrom(user.getEmail());

        return AuthSuccessResponse
                .builder()
                .token(token)
                .userId(user.getId())
                .build();
    }

    @LoggableAction(action = "GET_USER_INFO", affectedIdParam = "#id")
    public User findUserById(UUID id) {
        return userDomainService
                .findUserById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(id))
                );
    }

    @LoggableAction(action = "GET_USER_INFO")
    public User findUserByEmail(String email) {
        return userDomainService
                .findUserByEmail(email)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with email %s was not found".formatted(email))
                );
    }

    @LoggableAction(action = "GET_USER_INFO")
    public User findUserByPhoneNumber(String number) {
        return userDomainService
                .findUserByNumber(number)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with number %s was not found".formatted(number))
                );
    }

    @LoggableAction(action = "GET_USER_INFO", affectedIdParam = "#id")
    public List<String> getLicenceUrlsByBuyerId(UUID id) {
        return userDomainService
                .findLicencesByBuyerId(id)
                .stream()
                .map(fileStorageFacade::generatePresignedUrl)
                .toList();
    }

    @LoggableAction(action = "UPDATE_ACCOUNT", affectedIdParam = "#id")
    public void setIsAccountLocked(UUID id, boolean isAccountLocked) {
        userDomainService.setIsAccountLocked(id, isAccountLocked);
    }

    @LoggableAction(action = "GET_USER_INFO")
    public List<User> findAllUsers() {
        return userDomainService.findAllUsers();
    }

    @LoggableAction(action = "GET_USER_INFO")
    public List<SellerUser> findAllSellers() {
        return userDomainService.findAllSellers();
    }

    @LoggableAction(action = "GET_USER_INFO")
    public List<BuyerCompanyUser> findAllBuyerCompanies() {
        return userDomainService.findAllBuyerCompanies();
    }

    @LoggableAction(action = "GET_USER_INFO")
    public List<BuyerCompanyUser> findAllBuyerCompaniesByLocked(Boolean isLocked) {
        return userDomainService.findAllBuyerCompaniesByLocked(isLocked);
    }

    @LoggableAction(action = "GET_USER_INFO", affectedIdParam = "#id")
    public BuyerRepresentativeUser getRepresentativeById(UUID id) {
        return userDomainService.findRepresentativeById(id);
    }

    @LoggableAction(action = "GET_USER_INFO", affectedIdParam = "buyerCompanyId")
    public BuyerCompanyUser getCompanyById(UUID buyerCompanyId) {
        return userDomainService.findBuyerById(buyerCompanyId);
    }

    @LoggableAction(action = "DELETE_USER", affectedIdParam = "#id")
    public void deleteUserById(UUID id) {
        User userToDelete = userDomainService
                .findUserById(id)
                .orElseThrow(() -> new UserDoesNotExistException("User with id %s was not found".formatted(id)));

        userDomainService.deleteUserById(userToDelete.getId());

        if (userToDelete instanceof BuyerCompanyUser buyerCompanyUser) {
            List<String> fileUrls = buyerCompanyUser.getOrganisationLicenceURLs();
            fileStorageFacade.deleteFiles(fileUrls);
        }
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

    @LoggableAction(action = "UPDATE_ACCOUNT", affectedIdParam = "#userToUpdate.id")
    public User updateUser(User userToUpdate) {
        User user = userDomainService
                .findUserById(userToUpdate.getId())
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(userToUpdate.getId()))
                );

        user.applyChangesFrom(userToUpdate);

        return userDomainService.updateUser(user);
    }

    @LoggableAction(action = "UPDATE_ACCOUNT", affectedIdParam = "#id")
    public void updatePasswordById(UUID id, String rawPassword) {
        User user = userDomainService
                .findUserById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(id))
                );

        String hashedPassword = authenticator.generateHash(rawPassword);
        user.setPassword(hashedPassword);

        userDomainService.updateUser(user);
    }


    @LoggableAction(action = "UPDATE_ACCOUNT", affectedIdParam = "#userToUpdate.id")
    public User updateUserAsAdmin(User userToUpdate) {
        User user = userDomainService
                .findUserById(userToUpdate.getId())
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(userToUpdate.getId()))
                );

        user.applyChangesFrom(userToUpdate);
        user.setEmail(userToUpdate.getEmail());

        return userDomainService.updateUser(user);
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = authenticator.generateHash(user.getPassword());
        user.setPassword(encodedPassword);
    }


    @Async("eventTaskExecutor")
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

    @Async("eventTaskExecutor")
    @TransactionalEventListener
    public void cleanDatabaseRelationsOn(CarDeletedEvent event) {
        userDomainService.deleteOneTimeSellerByCarId(event.carId());
        userDomainService.deleteCarIdFromSeller(event.carId());
    }

    @Async("eventTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void rollbackUserByIdOn(CarSaveFailedRollbackUserEvent event) {
        userDomainService.deleteUserById(event.userId());
    }

    @Async("eventTaskExecutor")
    @TransactionalEventListener
    public void addSavedCarOn(NewBidPlacedEvent event) {
        userDomainService.addCarIdToSaved(event.bidderId(), event.carId());
    }
}

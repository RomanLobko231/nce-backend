package com.nce.backend.users.application;

import com.nce.backend.common.events.CarDeletedEvent;
import com.nce.backend.common.events.CarSaveFailedRollbackUserEvent;
import com.nce.backend.common.events.NewCarSavedEvent;
import com.nce.backend.file_storage.FileStorageFacade;
import com.nce.backend.security.SecurityFacade;
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
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final SecurityFacade securityFacade;
    private final UserDomainService userDomainService;
    private final FileStorageFacade fileStorageFacade;

    public User registerSeller(SellerUser seller) {
        if (userDomainService.existsByEmail(seller.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        this.setEncodedPassword(seller);

        return userDomainService.registerSeller(seller);
    }

    public User registerBuyerCompany(BuyerCompanyUser buyerCompany, List<MultipartFile> organisationLicences) {
        if (userDomainService.existsByEmail(buyerCompany.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        this.setEncodedPassword(buyerCompany);

        List<String> filesUrls = fileStorageFacade.uploadFiles(organisationLicences);
        buyerCompany.setOrganisationLicenceURLs(filesUrls);

        return userDomainService.registerBuyerCompany(buyerCompany);
    }


    public User registerRepresentative(BuyerRepresentativeUser representative) {
        if (userDomainService.existsByEmail(representative.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        this.setEncodedPassword(representative);

        return userDomainService.registerRepresentative(representative);
    }

    public User registerOneTimeSeller(OneTimeSellerUser oneTimeSeller) {
        String randomEmail = UUID.randomUUID().toString();
        String randomPassword = securityFacade.generateHash(UUID.randomUUID().toString());

        oneTimeSeller.setEmail(randomEmail);
        oneTimeSeller.setPassword(randomPassword);

        return userDomainService.registerOneTimeSeller(oneTimeSeller);
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

    public User findUserById(UUID id) {
        return userDomainService
                .findUserById(id)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(id))
                );
    }

    public User findUserByEmail(String email) {
        return userDomainService
                .findUserByEmail(email)
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with email %s was not found".formatted(email))
                );
    }

    public List<String> getLicenceUrlsByBuyerId(UUID id) {
        return userDomainService
                .findLicencesByBuyerId(id)
                .stream()
                .map(fileStorageFacade::generatePresignedUrl)
                .toList();
    }

    public void setIsAccountLocked(UUID id, boolean isAccountLocked) {
        userDomainService.setIsAccountLocked(id, isAccountLocked);
    }

    public List<User> findAllUsers() {
        return userDomainService.findAllUsers();
    }

    public List<SellerUser> findAllSellers() {
        return userDomainService.findAllSellers();
    }

    public List<BuyerCompanyUser> findAllBuyerCompanies() {
        return userDomainService.findAllBuyerCompanies();
    }

    public List<BuyerCompanyUser> findAllBuyerCompaniesByLocked(Boolean isLocked) {
        return userDomainService.findAllBuyerCompaniesByLocked(isLocked);
    }

    public BuyerRepresentativeUser getRepresentativeById(UUID id) {
        return userDomainService.findRepresentativeById(id);
    }

    public BuyerCompanyUser getCompanyById(UUID buyerCompanyId) {
        return userDomainService.findBuyerById(buyerCompanyId);
    }

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

    public User updateUser(User userToUpdate) {
        User user = userDomainService
                .findUserById(userToUpdate.getId())
                .orElseThrow(
                        () -> new UserDoesNotExistException("User with id %s was not found".formatted(userToUpdate.getId()))
                );

        //todo: make verify integrity polymorphic method
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setAccountLocked(user.isAccountLocked());
        if (user instanceof BuyerCompanyUser buyerCompanyUser) {
            ((BuyerCompanyUser) userToUpdate)
                    .setCompanyRepresentatives(buyerCompanyUser.getCompanyRepresentatives());
        }

        return userDomainService.updateUser(userToUpdate);
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = securityFacade.generateHash(user.getPassword());
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

}

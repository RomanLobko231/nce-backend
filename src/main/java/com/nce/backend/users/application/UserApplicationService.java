package com.nce.backend.users.application;

import com.nce.backend.common.events.NewCarSavedEvent;
import com.nce.backend.file_storage.FileStorageFacade;
import com.nce.backend.security.SecurityFacade;
import com.nce.backend.users.domain.entities.BuyerUser;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.repositories.SellerUserRepository;
import com.nce.backend.users.domain.services.UserDomainService;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.ui.requests.LoginRequest;
import com.nce.backend.users.ui.responses.AuthSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final SecurityFacade securityFacade;
    private final UserDomainService userDomainService;
    private final FileStorageFacade fileStorageFacade;

    public SellerUser registerSeller(SellerUser userToRegister) {
        setEncodedPassword(userToRegister);

        return userDomainService.saveSeller(userToRegister);
    }

    public BuyerUser registerBuyer(BuyerUser userToRegister, List<MultipartFile> organisationLicences) {
        setEncodedPassword(userToRegister);
        if (organisationLicences != null && !organisationLicences.isEmpty()) {
            List<String> filesUrls = fileStorageFacade.uploadFiles(organisationLicences);
            userToRegister.setOrganisationLicenceURLs(filesUrls);
        }

        return userDomainService.saveBuyer(userToRegister);
    }

    public AuthSuccessResponse authenticateUser(LoginRequest request) {
        User user = userDomainService
                .findUserByEmail(request.email())
                .orElseThrow(
                        () -> new NoSuchElementException("User with email %s was not found".formatted(request.email()))
                );

        if(!securityFacade.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = securityFacade.generateToken(user.getId(), user.getEmail());

        return AuthSuccessResponse
                .builder()
                .token(token)
                .userId(user.getId())
                .build();
    }

    @Async
    @TransactionalEventListener
    public void addCarToUserOn(NewCarSavedEvent event){
        SellerUser user = userDomainService
                .findSellerById(event.ownerId())
                .orElseThrow(
                        () -> new NoSuchElementException("User with id %s was not found".formatted(event.ownerId()))
                );
        user.addCarId(event.carId());

        userDomainService.saveUser(user);
    }

    private void setEncodedPassword(User user) {
        String encodedPassword = securityFacade.encodePassword(user.getPassword());
        user.setPassword(encodedPassword);
    }
}

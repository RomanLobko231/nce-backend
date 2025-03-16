package com.nce.backend.users;

import com.nce.backend.security.SecurityFacade;
import com.nce.backend.users.application.UserApplicationService;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.entities.User;
import com.nce.backend.users.domain.services.UserDomainService;
import com.nce.backend.users.domain.valueObjects.Address;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.ui.requests.RegisterSellerRequest;
import com.nce.backend.users.ui.requests.UserRequestMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserApplicationServiceTest {

    @Autowired
    private UserApplicationService userApplicationService;

    @Autowired
    private UserRequestMapper userRequestMapper;

    @Test
    @Transactional
    void testRegisterSeller_ReturnsSuccessfully() {
        RegisterSellerRequest request = new RegisterSellerRequest(
                "Name",
                "09856432",
                "email@email.com",
                "password",
                Address
                        .builder()
                        .postalCode("0987")
                        .postalLocation("City")
                        .streetAddress("Street Address, 123")
                        .build()
        );

        SellerUser mappedUser = userRequestMapper.toSellerFromRequest(request);
        SellerUser savedUser = userApplicationService.registerSeller(mappedUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getPassword());
        assertEquals(Role.SELLER, savedUser.getRole());
        assertEquals(request.name(), savedUser.getName());
        assertEquals(request.phoneNumber(), savedUser.getPhoneNumber());
        assertEquals(request.email(), savedUser.getEmail());
        assertEquals(request.address(), savedUser.getSellerAddress());
    }
}

package com.nce.backend.users;

import com.nce.backend.users.application.service.UserApplicationService;
import com.nce.backend.users.domain.entities.SellerUser;
import com.nce.backend.users.domain.valueObjects.Role;
import com.nce.backend.users.ui.requests.address.ValidatedAddress;
import com.nce.backend.users.ui.requests.register.RegisterSellerRequest;
import com.nce.backend.users.ui.requests.UserRequestMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
                ValidatedAddress
                        .builder()
                        .postalCode("0987")
                        .postalLocation("City")
                        .streetAddress("Street Address, 123")
                        .build()
        );

        SellerUser mappedUser = userRequestMapper.toSellerFromRegister(request);
        SellerUser savedUser = (SellerUser) userApplicationService.registerSeller(mappedUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getPassword());
        assertEquals(Role.SELLER, savedUser.getRole());
        assertEquals(request.name(), savedUser.getName());
        assertEquals(request.phoneNumber(), savedUser.getPhoneNumber());
        assertEquals(request.email(), savedUser.getEmail());
        assertFalse(savedUser.isAccountLocked());
    }
}

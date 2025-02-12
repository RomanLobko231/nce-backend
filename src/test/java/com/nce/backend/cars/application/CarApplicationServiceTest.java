package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.ui.requests.AddCarAdminRequest;
import com.nce.backend.cars.ui.requests.AddCarCustomerRequest;
import com.nce.backend.cars.ui.requests.CarRequestMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CarApplicationServiceTest {

    @Autowired
    private CarApplicationService carApplicationService;

    @Autowired
    private CarRequestMapper carRequestMapper;

    @Test
    @Transactional
    void testAddCarAsCustomer_savesCarSuccessfully() {
        AddCarCustomerRequest request = new AddCarCustomerRequest(
                "John Doe",
                "123456789",
                "ABC123",
                "john.doe@example.com",
                10
        );

        Car mappedCar = carRequestMapper.toCarFromCustomerRequest(request);
        Car savedCar = carApplicationService.addCarAsCustomer(mappedCar);

        assertNotNull(savedCar);
        assertEquals("ABC123", savedCar.getRegistrationNumber());
        assertEquals("John Doe", savedCar.getOwnerInfo().name());
        assertEquals("123456789", savedCar.getOwnerInfo().phoneNumber());
        assertEquals("john.doe@example.com", savedCar.getOwnerInfo().email());
        assertEquals(10, savedCar.getKilometers());
    }

    @Test
    @Transactional
    void testAddCarAsAdmin_savesCarSuccessfully() {
        AddCarAdminRequest request = new AddCarAdminRequest(
                "ABC123",
                10,
                "Make",
                "Model",
                LocalDate.now().minusMonths(6),
                "Diesel",
                0,
                "Kupe",
                5,
                4,
                "Black",
                "Manuell",
                "Framhjulstrekk",
                1000,
                LocalDate.now().plusMonths(6),
                OwnerInfo.builder()
                        .email("email")
                        .phoneNumber("123454567")
                        .name("Owner")
                        .build(),
                "In Review",
                null,
                Collections.emptyList()
        );
        Car mappedCar = carRequestMapper.toCarFromAdminRequest(request);
        Car savedCar = carApplicationService.addCarAsAdmin(mappedCar, null);

        assertNotNull(savedCar);
        assertNotNull(savedCar.getId());
        assertEquals("ABC123", savedCar.getRegistrationNumber());
        assertEquals(10, savedCar.getKilometers());
        assertEquals("Make", savedCar.getMake());
        assertEquals("Model", savedCar.getModel());
        assertEquals("Diesel", savedCar.getEngineType());
        assertEquals(0, savedCar.getEngineVolume());
        assertEquals("Kupe", savedCar.getBodywork());
        assertEquals("Black", savedCar.getColor());
        assertEquals("Manuell", savedCar.getGearboxType().getValue());
        assertEquals("Framhjulstrekk", savedCar.getOperatingMode().getValue());
        assertEquals(1000, savedCar.getWeight());
        assertEquals("In Review", savedCar.getStatus().getValue());
        assertEquals(LocalDate.now().minusMonths(6), savedCar.getFirstTimeRegisteredInNorway());
        assertEquals("email", savedCar.getOwnerInfo().email());
        assertEquals("123454567", savedCar.getOwnerInfo().phoneNumber());
        assertEquals("Owner", savedCar.getOwnerInfo().name());
        assertNull(savedCar.getAdditionalInformation());
        assertEquals(Collections.emptyList(), savedCar.getImagePaths());


    }
}

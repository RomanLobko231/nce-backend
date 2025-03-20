package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.ui.requests.AddCarAdminRequest;
import com.nce.backend.cars.ui.requests.AddCarCustomerRequest;
import com.nce.backend.cars.ui.requests.CarRequestMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
               UUID.randomUUID(),
                "ABC123",
                10
        );

        Car mappedCar = carRequestMapper.toCarFromCustomerRequest(request);
        Car savedCar = carApplicationService.addCarAsCustomer(mappedCar, Collections.emptyList());

        assertNotNull(savedCar);
        assertEquals("ABC123", savedCar.getRegistrationNumber());
        assertNotNull(savedCar.getOwnerID());
        assertEquals(10, savedCar.getKilometers());
        assertEquals(Collections.emptyList(), savedCar.getImagePaths());
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
                UUID.randomUUID(),
                "Vurdering",
                null,
                Collections.emptyList()
        );
        Car mappedCar = carRequestMapper.toCarFromAdminRequest(request);
        Car savedCar = carApplicationService.addCarAsAdmin(mappedCar, Collections.emptyList());

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
        assertEquals("Vurdering", savedCar.getStatus().getValue());
        assertEquals(LocalDate.now().minusMonths(6), savedCar.getFirstTimeRegisteredInNorway());
        assertNotNull(savedCar.getOwnerID());
        assertNull(savedCar.getAdditionalInformation());
        assertEquals(Collections.emptyList(), savedCar.getImagePaths());
    }
}

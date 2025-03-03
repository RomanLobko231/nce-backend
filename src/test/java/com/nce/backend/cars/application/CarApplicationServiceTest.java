package com.nce.backend.cars.application;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.services.ImageStorageService;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.infrastructure.aws.S3ImageStorageService;
import com.nce.backend.cars.ui.requests.AddCarAdminRequest;
import com.nce.backend.cars.ui.requests.AddCarCustomerRequest;
import com.nce.backend.cars.ui.requests.CarRequestMapper;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarApplicationServiceTest {


    @Autowired
    private CarApplicationService carApplicationService;

    @Autowired
    private CarRequestMapper carRequestMapper;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

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
        Car savedCar = carApplicationService.addCarAsCustomer(mappedCar, Collections.emptyList());

        assertNotNull(savedCar);
        assertEquals("ABC123", savedCar.getRegistrationNumber());
        assertEquals("John Doe", savedCar.getOwnerInfo().name());
        assertEquals("123456789", savedCar.getOwnerInfo().phoneNumber());
        assertEquals("john.doe@example.com", savedCar.getOwnerInfo().email());
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
                OwnerInfo.builder()
                        .email("email")
                        .phoneNumber("123454567")
                        .name("Owner")
                        .build(),
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
        assertEquals("email", savedCar.getOwnerInfo().email());
        assertEquals("123454567", savedCar.getOwnerInfo().phoneNumber());
        assertEquals("Owner", savedCar.getOwnerInfo().name());
        assertNull(savedCar.getAdditionalInformation());
        assertEquals(Collections.emptyList(), savedCar.getImagePaths());
    }
}

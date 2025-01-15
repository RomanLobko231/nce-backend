package com.nce.backend.cars;

import com.nce.backend.cars.application.CarApplicationService;
import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.ui.requests.AddCarRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CarApplicationServiceTest {

    @Autowired
    private CarApplicationService carApplicationService;

    @Test
    @Transactional
    public void testAddNewCar_savesCarSuccessfully() {

        AddCarRequest addCarRequest = new AddCarRequest(
                "John Doe",
                "123456789",
                "ABC123",
                "john.doe@example.com"
        );

        Car savedCar = carApplicationService.addNewCarRequest(addCarRequest);

        assertNotNull(savedCar);
        assertEquals("ABC123", savedCar.getRegistrationNumber());
        assertEquals("John Doe", savedCar.getOwnerInfo().name());
        assertEquals("123456789", savedCar.getOwnerInfo().phoneNumber());
        assertEquals("john.doe@example.com", savedCar.getOwnerInfo().email());
    }
}

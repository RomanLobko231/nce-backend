package com.nce.backend.cars.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nce.backend.cars.application.CarApplicationService;
import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.OwnerInfo;
import com.nce.backend.cars.ui.requests.AddCarAdminRequest;
import com.nce.backend.cars.ui.requests.AddCarCustomerRequest;
import com.nce.backend.cars.ui.requests.CarRequestMapper;
import com.nce.backend.cars.ui.requests.UpdateCarRequest;
import com.nce.backend.cars.ui.responses.CarResponse;
import com.nce.backend.cars.ui.responses.CarResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarApplicationService carService;

    @MockitoBean
    private CarResponseMapper carResponseMapper;

    @MockitoBean
    private CarRequestMapper carRequestMapper;

    @Test
    public void testAddCarAsCustomer() throws Exception {
        AddCarCustomerRequest request = new AddCarCustomerRequest(
                "John Doe",
                "123456789",
                "ABC123",
                "john.doe@example.com",
                10
        );

        mockMvc.perform(post("/api/v1/cars/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(carService).addCarAsCustomer(carRequestMapper.toCarFromCustomerRequest(request));
    }

    @Test
    public void testAddCarAsAdmin() throws Exception {
        AddCarAdminRequest carData = new AddCarAdminRequest(
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
                10,
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

        MockMultipartFile carDataPart = new MockMultipartFile(
                "carData",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().findAndRegisterModules().writeValueAsString(carData).getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                "image1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy-image-content1".getBytes()
        );

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                "image2.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy-image-content2".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/cars/admin")
                        .file(carDataPart)
                        .file(image1)
                        .file(image2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService).addCarAsAdmin(carRequestMapper.toCarFromAdminRequest(carData), List.of(image1, image2));
    }

    @Test
    public void testUpdateCar() throws Exception {
        UpdateCarRequest carData = new UpdateCarRequest(
                UUID.randomUUID(),
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

        MockMultipartFile carDataPart = new MockMultipartFile(
                "carData",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().findAndRegisterModules().writeValueAsString(carData).getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                "image1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy-image-content1".getBytes()
        );

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                "image2.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy-image-content2".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/cars")
                        .file(carDataPart)
                        .file(image1)
                        .file(image2)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(carService).updateCar(carRequestMapper.toCarFromUpdateRequest(carData), List.of(image1, image2));
    }

    @Test
    public void testGetAllCars() throws Exception {
        List<CarResponse> carResponses = List.of(CarResponse.builder().build());

        when(carService.getAllCars()).thenReturn(List.of(Car.builder().build()));
        when(carResponseMapper.toCarResponse(any())).thenReturn(carResponses.get(0));

        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(carResponses.size()));
    }

    @Test
    public void testGetCarById() throws Exception {
        UUID carId = UUID.randomUUID();
        CarResponse carResponse = CarResponse.builder().id(carId).build();

        when(carService.findById(carId)).thenReturn(Car.builder().build());
        when(carResponseMapper.toCarResponse(any())).thenReturn(carResponse);

        mockMvc.perform(get("/api/v1/cars/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carId.toString()));
    }

    @Test
    public void testDeleteCar() throws Exception {
        UUID carId = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/cars/{id}", carId))
                .andExpect(status().isOk());

        verify(carService).deleteById(carId);
    }
}

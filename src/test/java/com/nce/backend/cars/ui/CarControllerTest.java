package com.nce.backend.cars.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nce.backend.cars.application.CarApplicationService;
import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.ui.requests.AddCarAdminRequest;
import com.nce.backend.cars.ui.requests.AddCarSimplifiedRequest;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
        AddCarSimplifiedRequest request = new AddCarSimplifiedRequest(
                UUID.randomUUID(),
                "ABC123",
                10,
                10000
        );

        MockMultipartFile carDataPart = new MockMultipartFile(
                "carData",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().findAndRegisterModules().writeValueAsString(request).getBytes(StandardCharsets.UTF_8)
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

        Car mockCar = Car
                .builder()
                .registrationNumber(request.registrationNumber())
                .kilometers(request.kilometers())
                .build();

        when(carRequestMapper.toCarFromCustomerSimpleRequest(any(AddCarSimplifiedRequest.class))).thenReturn(mockCar);
        when(carService.addCarSimplified(any(Car.class), anyList())).thenReturn(mockCar);

        mockMvc.perform(multipart("/api/v1/cars/customer")
                        .file(carDataPart)
                        .file(image1)
                        .file(image2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService).addCarSimplified(mockCar, List.of(image1, image2));
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
                UUID.randomUUID(),
                10000,
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
        Car mockCar = Car.builder().build();

        when(carRequestMapper.toCarFromAdminRequest(any(AddCarAdminRequest.class))).thenReturn(mockCar);
        when(carService.addCarComplete(any(Car.class), anyList())).thenReturn(mockCar);

        mockMvc.perform(multipart("/api/v1/cars/admin")
                        .file(carDataPart)
                        .file(image1)
                        .file(image2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(carService).addCarComplete(mockCar, List.of(image1, image2));
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
                UUID.randomUUID(),
                1000,
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

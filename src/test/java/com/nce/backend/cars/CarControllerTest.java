package com.nce.backend.cars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nce.backend.cars.application.CarApplicationService;
import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.ui.requests.AddCarRequest;
import com.nce.backend.cars.ui.responses.CarResponse;
import com.nce.backend.cars.ui.responses.CarResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    public void testAddNewCar() throws Exception {
        AddCarRequest request = new AddCarRequest(
                "John Doe",
                "123456789",
                "ABC123",
                "john.doe@example.com"
        );

        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(carService).addNewCarRequest(any(AddCarRequest.class));
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

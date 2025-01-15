package com.nce.backend.cars.ui;

import com.nce.backend.cars.application.CarApplicationService;
import com.nce.backend.cars.ui.requests.AddCarRequest;
import com.nce.backend.cars.ui.responses.CarResponse;
import com.nce.backend.cars.ui.responses.CarResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarApplicationService carService;
    private final CarResponseMapper carResponseMapper;

    @PostMapping
    ResponseEntity<Void> addNewCar(@RequestBody @Valid AddCarRequest addCarRequest) {
        carService.addNewCarRequest(addCarRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<CarResponse>> getAllCars() {
        List<CarResponse> fetchedCars = carService
                .getAllCars()
                .stream()
                .map(carResponseMapper::toCarResponse)
                .toList();

        return ResponseEntity.ok(fetchedCars);
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<CarResponse> getCarById(@PathVariable UUID id) {
        CarResponse fetchedCar = carResponseMapper.toCarResponse(carService.findById(id));

        return ResponseEntity.ok(fetchedCar);
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

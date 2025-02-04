package com.nce.backend.cars.ui;

import com.nce.backend.cars.application.CarApplicationService;
import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.ui.requests.AddCarAdminRequest;
import com.nce.backend.cars.ui.requests.AddCarCustomerRequest;
import com.nce.backend.cars.ui.requests.CarRequestMapper;
import com.nce.backend.cars.ui.requests.UpdateCarRequest;
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
@CrossOrigin(origins = "*")
public class CarController {

    private final CarApplicationService carService;
    private final CarResponseMapper carResponseMapper;
    private final CarRequestMapper carRequestMapper;

    @PostMapping("/customer")
    ResponseEntity<Void> addNewCarAsCustomer(@RequestBody @Valid AddCarCustomerRequest request) {
        carService.addCarAsCustomer(carRequestMapper.toCarFromCustomerRequest(request));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin")
    ResponseEntity<Void> addNewCarAsAdmin(@RequestBody @Valid AddCarAdminRequest request) {
        carService.addCarAsAdmin(carRequestMapper.toCarFromAdminRequest(request));

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

    @PutMapping()
    ResponseEntity<Void> updateCar(@RequestBody @Valid UpdateCarRequest updateCarRequest) {
        Car carToUpdate = carRequestMapper.toCarFromUpdateRequest(updateCarRequest);
        carService.updateCar(carToUpdate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

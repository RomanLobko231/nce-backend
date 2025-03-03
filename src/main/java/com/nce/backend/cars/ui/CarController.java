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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/customer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> addNewCarAsCustomer(
            @RequestPart(name = "request") @Valid AddCarCustomerRequest request,
            @RequestPart(name = "images", required = false) List<MultipartFile> images
            ) {
        carService.addCarAsCustomer(carRequestMapper.toCarFromCustomerRequest(request), images);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> addNewCarAsAdmin(
            @RequestPart(name = "carData") @Valid AddCarAdminRequest carData,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        carService.addCarAsAdmin(
                carRequestMapper.toCarFromAdminRequest(carData),
                images
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/add-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> addImagesForCar(
            @RequestParam(name = "carId") UUID id,
            @RequestParam(name = "images") List<MultipartFile> images) {


        carService.addImagesForCarById(id, images);
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

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> updateCar(@RequestPart(name = "carData", required = true) @Valid UpdateCarRequest carData,
                                   @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Car carToUpdate = carRequestMapper.toCarFromUpdateRequest(carData);
        carService.updateCar(carToUpdate, images);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

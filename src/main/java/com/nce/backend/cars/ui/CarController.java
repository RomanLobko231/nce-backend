package com.nce.backend.cars.ui;

import com.nce.backend.cars.application.service.CarApplicationService;
import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.cars.domain.valueObjects.PaginatedResult;
import com.nce.backend.cars.domain.valueObjects.Status;
import com.nce.backend.cars.ui.requests.*;
import com.nce.backend.cars.ui.responses.CarResponse;
import com.nce.backend.cars.ui.responses.CarResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarApplicationService carService;
    private final CarResponseMapper carResponseMapper;
    private final CarRequestMapper carRequestMapper;

    @PostMapping(value = "/add-simplified", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> addNewCarSimplified(
            @RequestPart(name = "carData") @Valid AddCarSimplifiedRequest request,
            @RequestPart(name = "images", required = false) List<MultipartFile> images
    ) {
        carService.addCarSimplified(
                carRequestMapper.toCarFromCustomerSimpleRequest(request),
                images
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/add-complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CarResponse> addNewCarComplete(
            @RequestPart(name = "carData") @Valid AddCarAdminRequest carData,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Car savedCar = carService.addCarComplete(
                carRequestMapper.toCarFromAdminRequest(carData),
                images
        );

        return ResponseEntity.ok(carResponseMapper.toCarResponse(savedCar));
    }

    @PostMapping(value = "/add-complete-user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<CarResponse> addNewCarCompleteAsUser(
            @RequestPart(name = "carData") @Valid AddCarCustomerRequest carData,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Car savedCar = carService.addCarComplete(
                carRequestMapper.toCarFromCustomerRequest(carData),
                images
        );

        return ResponseEntity.ok(carResponseMapper.toCarResponse(savedCar));
    }

    @PostMapping(value = "/add-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> addImagesForCar(
            @RequestParam(name = "carId") UUID id,
            @RequestParam(name = "images") List<MultipartFile> images) {

        carService.addImagesForCarById(id, images);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<PaginatedResult<CarResponse>> getAllByOwnerAndStatus(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "ownerId", required = false) UUID ownerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size
    ) {
        PaginatedResult<Car> result;
        Status carStatus = Status.fromString(status);

        if (ownerId != null && status != null) {
            result = carService.getAllCarsByOwnerAndStatus(carStatus, ownerId, page, size);
        } else if (ownerId != null) {
            result = carService.getAllCarsByOwnerId(ownerId, page, size);
        } else if (status != null) {
            result = carService.getAllCarsByStatus(carStatus, page, size);
        } else {
            result = carService.getAllCars(page, size);
        }

        return ResponseEntity.ok(carResponseMapper.toCarResponsePaginated(result));
    }

    @GetMapping(value = "/by-owner/{ownerId}")
    @PreAuthorize("#ownerId == authentication.principal.id or hasRole('ROLE_ADMIN')")
    ResponseEntity<PaginatedResult<CarResponse>> getAllCarsByOwnerId(
            @PathVariable UUID ownerId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size
    ) {

        PaginatedResult<Car> result = carService.getAllCarsByOwnerId(ownerId, page, size);

        return ResponseEntity.ok(
                carResponseMapper.toCarResponsePaginated(result)
        );
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<CarResponse> getCarById(@PathVariable UUID id) {
        CarResponse fetchedCar = carResponseMapper.toCarResponse(carService.findById(id));

        return ResponseEntity.ok(fetchedCar);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("#carData.ownerId() == authentication.principal.id or hasRole('ROLE_ADMIN')")
    ResponseEntity<Void> updateCar(@RequestPart(name = "carData") @Valid UpdateCarRequest carData,
                                   @RequestPart(name = "images", required = false) List<MultipartFile> images) {

        Car carToUpdate = carRequestMapper.toCarFromUpdateRequest(carData);
        carService.updateCar(carToUpdate, images);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{carId}")
    ResponseEntity<Void> updateCarStatus(
            @RequestParam(name = "status", required = true) String status,
            @PathVariable UUID carId
    ) {

        carService.updateCarStatus(Status.fromString(status), carId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/exists")
    ResponseEntity<Boolean> existsByRegNumber(@RequestParam(name = "regNumber") String regNumber) {
        boolean exists = carService.existsByRegNumber(regNumber);

        return ResponseEntity.ok(exists);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

package com.nce.backend.cars.ui;

import com.nce.backend.cars.ui.request.AddCarRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController {

    ResponseEntity<Void> addNewCar(@RequestBody AddCarRequest addCarRequest){

        return ResponseEntity.ok().build();
    }
}

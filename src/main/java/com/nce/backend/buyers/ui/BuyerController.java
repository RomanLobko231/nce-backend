package com.nce.backend.buyers.ui;

import com.nce.backend.buyers.ui.requests.RegisterBuyerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/buyers")
public class BuyerController {

    @PostMapping
    ResponseEntity<Void> registerNewBuyer(RegisterBuyerRequest request){
        return ResponseEntity.ok().build();
    }
}

package com.nce.backend.auction.ui.rest;

import com.nce.backend.auction.application.AuctionApplicationService;
import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.ui.rest.requests.AuctionRequestMapper;
import com.nce.backend.auction.ui.rest.requests.NewAuctionRequest;
import com.nce.backend.auction.ui.rest.requests.UpdateAuctionRequest;
import com.nce.backend.auction.ui.rest.responses.AuctionResponse;
import com.nce.backend.auction.ui.rest.responses.AuctionResponseMapper;
import com.nce.backend.cars.domain.valueObjects.Status;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auctions")
public class AuctionController {

    private final AuctionApplicationService applicationService;
    private final AuctionRequestMapper requestMapper;
    private final AuctionResponseMapper responseMapper;

    @PostMapping("/start")
    ResponseEntity<Void> startAuction(@Valid @RequestBody NewAuctionRequest request) {
        applicationService.startAuction(
                requestMapper.toDomainFromNew(request)
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    ResponseEntity<List<AuctionResponse>> getAllByStatus(
            @RequestParam(value = "status", required = true) String status
    ) {
        List<AuctionResponse> response = applicationService
                .getAllAuctionsByStatus(AuctionStatus.fromString(status))
                .stream()
                .map(responseMapper::toAuctionResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<AuctionResponse> getAuctionById(@PathVariable UUID id) {
        Auction auction = applicationService.getAuctionById(id);

        return ResponseEntity.ok(responseMapper.toAuctionResponse(auction));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAuctionById(@PathVariable UUID id) {
        applicationService.deleteAuctionById(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/by_car/{carId}")
    ResponseEntity<Auction> getAuctionByCarId(@PathVariable UUID carId) {
        Auction auction = applicationService.getAuctionByCarId(carId);

        return ResponseEntity.ok(auction);
    }

    @PutMapping()
    ResponseEntity<Void> updateAuction(@Valid @RequestBody UpdateAuctionRequest request) {
        applicationService.updateAuction(
                requestMapper.toDomainFromUpdate(request)
        );

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{auctionId}")
    ResponseEntity<Void> updateAuctionStatus(
            @RequestParam(name = "status", required = true) String status,
            @PathVariable UUID auctionId
    ) {

        applicationService.updateAuctionStatus(AuctionStatus.fromString(status), auctionId);
        return ResponseEntity.ok().build();
    }

}

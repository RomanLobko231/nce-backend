package com.nce.backend.auction.ui.rest;

import com.nce.backend.auction.application.AuctionApplicationService;
import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.ui.rest.requests.AuctionRequestMapper;
import com.nce.backend.auction.ui.rest.requests.NewAuctionRequest;
import com.nce.backend.auction.ui.rest.requests.UpdateAuctionRequest;
import com.nce.backend.auction.ui.rest.responses.AuctionResponse;
import com.nce.backend.auction.ui.rest.responses.AuctionResponseMapper;
import com.nce.backend.auction.ui.websocket.requests.AutoBidMessage;
import com.nce.backend.auction.ui.websocket.requests.BidMessage;
import com.nce.backend.auction.ui.websocket.responses.AuctionUpdatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/place-bid")
    @PreAuthorize("authentication.principal.isAccountLocked == false")
    public ResponseEntity<AuctionResponse> placeBid(@RequestBody @Valid BidMessage bid) {
        Auction auction = applicationService.placeBid(
                bid.toDomain()
        );

        return ResponseEntity.ok(responseMapper.toAuctionResponse(auction));
    }

    @PostMapping("/place-auto-bid")
    @PreAuthorize("authentication.principal.isAccountLocked == false")
    public ResponseEntity<AuctionResponse> placeAutoBid(@RequestBody @Valid AutoBidMessage bid) {
        Auction auction = applicationService.placeAutoBid(
                bid.toDomain()
        );

        return ResponseEntity.ok(responseMapper.toAuctionResponse(auction));
    }


    @GetMapping()
    ResponseEntity<List<AuctionResponse>> getAllByStatus(
            @RequestParam(value = "status", required = true) String auctionStatus,
            @RequestParam(value = "ids", required = false) List<UUID> carIds
    ) {

        List<Auction> auctions;
        AuctionStatus status = AuctionStatus.fromString(auctionStatus);

        if(carIds != null && !carIds.isEmpty()) {
            auctions = applicationService.getAllByCarIdsAndStatus(carIds, status);
        } else {
            auctions = applicationService.getAllByStatus(status);
        }

        return ResponseEntity.ok(responseMapper.toAuctionResponses(auctions));
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

    @GetMapping("/by-car/{carId}")
    ResponseEntity<Auction> getAuctionByCarId(@PathVariable UUID carId) {
        Auction auction = applicationService.getAuctionByCarId(carId);

        return ResponseEntity.ok(auction);
    }

    @PutMapping()
    ResponseEntity<Void> updateAuction(@Valid @RequestBody UpdateAuctionRequest request) {
        applicationService.updateAuction(
                requestMapper.toDomainFromUpdate(request)
        );

        System.out.println(request.endDateTime().toString());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-restart")
    ResponseEntity<Void> restartUpdateAuction(@Valid @RequestBody UpdateAuctionRequest request) {
        applicationService.updateRestartAuction(
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

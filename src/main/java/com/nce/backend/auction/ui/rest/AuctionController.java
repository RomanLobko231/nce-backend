package com.nce.backend.auction.ui.rest;

import com.nce.backend.auction.application.service.AuctionApplicationService;
import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.PaginatedResult;
import com.nce.backend.auction.ui.rest.requests.AuctionRequestMapper;
import com.nce.backend.auction.ui.rest.requests.NewAuctionRequest;
import com.nce.backend.auction.ui.rest.requests.UpdateAuctionRequest;
import com.nce.backend.auction.ui.rest.responses.AuctionResponse;
import com.nce.backend.auction.ui.rest.responses.AuctionResponseMapper;
import com.nce.backend.auction.ui.websocket.requests.AutoBidMessage;
import com.nce.backend.auction.ui.websocket.requests.BidMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/by-status")
    ResponseEntity<PaginatedResult<AuctionResponse>> getAllByStatus(
            @RequestParam(value = "status", defaultValue = "Aktivt", required = false) String auctionStatus,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size

    ) {
        AuctionStatus status = AuctionStatus.fromString(auctionStatus);
        PaginatedResult<Auction> result = applicationService.getAllByStatus(status, page, size);

        List<AuctionResponse> response = result
                .getItems()
                .stream()
                .map(responseMapper::toAuctionResponse)
                .toList();

        return ResponseEntity.ok(
                new PaginatedResult<>(
                        response,
                        result.getTotalPages(),
                        result.getTotalElements(),
                        result.getCurrentPage()
                ));
    }


    @GetMapping()
    ResponseEntity<PaginatedResult<AuctionResponse>> getAllByStatusOrIds(
            @RequestParam(value = "status", defaultValue = "Aktivt", required = false) String auctionStatus,
            @RequestParam(value = "ids", required = false) List<UUID> carIds,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size

    ) {
        AuctionStatus status = AuctionStatus.fromString(auctionStatus);
        PaginatedResult<Auction> result;

        if (carIds != null && !carIds.isEmpty()) {
            result = applicationService.getAllByCarIdsAndStatus(carIds, status, page, size);
        } else {
            result = applicationService.getAllByStatus(status, page, size);
        }

        List<AuctionResponse> response = result
                .getItems()
                .stream()
                .map(responseMapper::toAuctionResponse)
                .toList();

        return ResponseEntity.ok(
                new PaginatedResult<>(
                response,
                result.getTotalPages(),
                result.getTotalElements(),
                result.getCurrentPage()
        ));
    }



    @GetMapping("/{id}")
    ResponseEntity<AuctionResponse> getAuctionById(@PathVariable UUID id) {
        Auction auction = applicationService.getAuctionById(id);

        return ResponseEntity.ok(responseMapper.toAuctionResponse(auction));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> updateAuction(@Valid @RequestBody UpdateAuctionRequest request) {
        applicationService.updateAuction(
                requestMapper.toDomainFromUpdate(request)
        );

        System.out.println(request.endDateTime().toString());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-restart")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> restartUpdateAuction(@Valid @RequestBody UpdateAuctionRequest request) {
        applicationService.updateRestartAuction(
                requestMapper.toDomainFromUpdate(request)
        );

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{auctionId}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> updateAuctionStatus(
            @RequestParam(name = "status", required = true) String status,
            @PathVariable UUID auctionId
    ) {

        applicationService.updateAuctionStatus(AuctionStatus.fromString(status), auctionId);
        return ResponseEntity.ok().build();
    }

}

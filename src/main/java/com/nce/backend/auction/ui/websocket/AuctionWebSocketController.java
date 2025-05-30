package com.nce.backend.auction.ui.websocket;

import com.nce.backend.auction.application.AuctionApplicationService;
import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.ui.websocket.requests.AutoBidMessage;
import com.nce.backend.auction.ui.websocket.requests.BidMessage;
import com.nce.backend.auction.ui.websocket.responses.AuctionUpdatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuctionWebSocketController {

    private final AuctionApplicationService applicationService;

    @MessageMapping("/place-bid")
    @SendTo("topic/auction-updates")
    @PreAuthorize("authentication.principal.isAccountLocked == false")
    public ResponseEntity<AuctionUpdatedResponse> handleBid(@RequestBody @Valid BidMessage bid) {
        Auction auction = applicationService.placeBid(
                bid.toDomain()
        );

        return ResponseEntity.ok(AuctionUpdatedResponse.fromDomain(auction));
    }

    @MessageMapping("/place-auto-bid")
    @SendTo("topic/auction-updates")
    @PreAuthorize("authentication.principal.isAccountLocked == false")
    public ResponseEntity<AuctionUpdatedResponse> handleAutoBid(@RequestBody @Valid AutoBidMessage bid) {
        Auction auction = applicationService.placeAutoBid(
                bid.toDomain()
        );

        return ResponseEntity.ok(AuctionUpdatedResponse.fromDomain(auction));
    }
}

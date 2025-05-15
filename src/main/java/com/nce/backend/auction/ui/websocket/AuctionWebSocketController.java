package com.nce.backend.auction.ui.websocket;

import com.nce.backend.auction.application.AuctionApplicationService;
import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.exceptions.AuctionClosedException;
import com.nce.backend.auction.ui.websocket.requests.BidMessage;
import com.nce.backend.auction.ui.websocket.responses.AuctionUpdateResponse;
import com.nce.backend.common.exception.ErrorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuctionWebSocketController {

    private final AuctionApplicationService applicationService;

    @MessageMapping("/place-bid")
    @SendTo("topic/auction-updates")
    public ResponseEntity<Auction> handleBid(@RequestBody @Valid BidMessage bid) {
        Auction auction = applicationService.placeBid(
                bid.toDomain()
        );

        return ResponseEntity.ok(auction);
    }
}

package com.nce.backend.auction.application.service;

import com.nce.backend.auction.application.port.Authenticator;
import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.service.AuctionDomainService;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.AutoBid;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.valueObjects.PaginatedResult;
import com.nce.backend.common.annotation.logging.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionApplicationService {

    private final AuctionDomainService auctionDomainService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Authenticator auctionSecurityService;

    public void startAuction(Auction auction) {
        auctionDomainService.startAuction(auction);
    }

    public void updateRestartAuction(Auction newAuction) {
        auctionDomainService.updateRestartAuction(newAuction);
    }

    public PaginatedResult<Auction> getAllByStatus(AuctionStatus status, int page, int size) {
        return auctionDomainService.getAllByStatus(status, page, size);
    }

    public PaginatedResult<Auction> getAllByCarIdsAndStatus(List<UUID> ids, AuctionStatus status, int page, int size) {
        return auctionDomainService.getAllByCarIdsAndStatus(ids, status, page, size);
    }

    public Auction getAuctionById(UUID id) {
        return auctionDomainService.getAuctionById(id);
    }

    public Auction getAuctionByCarId(UUID carId) {
        return auctionDomainService.getAuctionByCarId(carId);
    }

    public Auction placeBid(Bid bid) {
        UUID bidderId = auctionSecurityService.getCurrentUserId();
        bid.setBidderId(bidderId);

        Auction updatedAuction = auctionDomainService.placeBidOnAuction(bid);
        simpMessagingTemplate.convertAndSend("/topic/auction/" + updatedAuction.getCarDetails().getCarId(), updatedAuction);

        return updatedAuction;
    }

    public Auction placeAutoBid(AutoBid autoBid) {
        UUID bidderId = auctionSecurityService.getCurrentUserId();
        autoBid.setBidderId(bidderId);

        Auction updatedAuction = auctionDomainService.placeAutoBidOnAuction(autoBid);
        simpMessagingTemplate.convertAndSend("/topic/auction/" + updatedAuction.getCarDetails().getCarId(), updatedAuction);

        return updatedAuction;
    }

    public void updateAuctionStatus(AuctionStatus status, UUID auctionId) {
        auctionDomainService.updateAuctionStatus(status, auctionId);
    }

    @LoggableAction(action = "DELETE_AUCTION", affectedIdParam = "#id")
    public void deleteAuctionById(UUID id) {
        auctionDomainService.deleteById(id);
    }

    @LoggableAction(action = "UPDATE_AUCTION", affectedIdParam = "#auction.id")
    public void updateAuction(Auction auction) {
        auctionDomainService.updateAuction(auction);
    }

}

package com.nce.backend.auction.application;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.service.AuctionDomainService;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.AutoBid;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.security.SecurityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionApplicationService {

    private final AuctionDomainService auctionDomainService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SecurityFacade securityFacade;

    public void startAuction(Auction auction) {
        auctionDomainService.startAuction(auction);
    }

    public void updateRestartAuction(Auction newAuction) {
        auctionDomainService.updateRestartAuction(newAuction);
    }

    public List<Auction> getAllByStatus(AuctionStatus status) {
        return auctionDomainService.getAllByStatus(status);
    }

    public List<Auction> getAllByCarIdsAndStatus(List<UUID> ids, AuctionStatus status) {
        return auctionDomainService.getAllByCarIdsAndStatus(ids, status);
    }

    public Auction getAuctionById(UUID id) {
        return auctionDomainService.getAuctionById(id);
    }

    public Auction getAuctionByCarId(UUID carId) {
        return auctionDomainService.getAuctionByCarId(carId);
    }

    public Auction placeBid(Bid bid) {
        UUID bidderId = securityFacade.getCurrentUserId();
        bid.setBidderId(bidderId);

        Auction updatedAuction = auctionDomainService.placeBidOnAuction(bid);
        simpMessagingTemplate.convertAndSend("/topic/auction/" + updatedAuction.getCarDetails().getCarId(), updatedAuction);

        return updatedAuction;
    }

    public Auction placeAutoBid(AutoBid autoBid) {
        UUID bidderId = securityFacade.getCurrentUserId();
        autoBid.setBidderId(bidderId);

        Auction updatedAuction = auctionDomainService.placeAutoBidOnAuction(autoBid);
        simpMessagingTemplate.convertAndSend("/topic/auction/" + updatedAuction.getCarDetails().getCarId(), updatedAuction);

        return updatedAuction;
    }

    public void updateAuctionStatus(AuctionStatus status, UUID auctionId) {
        auctionDomainService.updateAuctionStatus(status, auctionId);
    }

    public void deleteAuctionById(UUID id) {
        auctionDomainService.deleteById(id);
    }

    public void updateAuction(Auction auction) {
        auctionDomainService.updateAuction(auction);
    }

}

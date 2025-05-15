package com.nce.backend.auction.application;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.service.AuctionDomainService;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.Bid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionApplicationService {

    private final AuctionDomainService auctionDomainService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void startAuction(Auction auction) {
        auctionDomainService.startAuction(auction);
    }

    public List<Auction> getAllAuctionsByStatus(AuctionStatus status) {
        return auctionDomainService.getAllAuctionsByStatus(status);
    }

    public Auction getAuctionById(UUID id) {
        return auctionDomainService.getAuctionById(id);
    }

    public Auction getAuctionByCarId(UUID carId) {
        return auctionDomainService.getAuctionByCarId(carId);
    }

    public Auction placeBid(Bid bid) {
        Auction updatedAuction = auctionDomainService.placeBidOnAuction(bid);

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

package com.nce.backend.auction.domain.service;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.repository.AuctionRepository;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.exceptions.AuctionClosedException;
import com.nce.backend.auction.exceptions.AuctionDoesNotExist;
import com.nce.backend.common.events.NewAuctionStarted;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionDomainService {

    private final AuctionRepository auctionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void startAuction(Auction auction) {
        if (auctionRepository.existsByCarId(auction.getCarDetails().getCarId())) {
            throw new IllegalStateException("Auction for car '%s' has already started".formatted(
                    auction.getCarDetails().getCarId())
            );
        }

        auction.setStatus(AuctionStatus.ACTIVE);
        Auction savedAuction = auctionRepository.save(auction);

        eventPublisher.publishEvent(
                new NewAuctionStarted(
                        savedAuction.getId(),
                        savedAuction.getCarDetails().getCarId(),
                        savedAuction.getEndDateTime()
                )
        );
    }

    public Auction saveAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    @Transactional
    public void deleteById(UUID id) {
        auctionRepository.deleteById(id);
    }

    @Transactional
    public Auction placeBidOnAuction(Bid bid) {
        Auction auction = auctionRepository
                .findById(bid.getAuctionId())
                .orElseThrow(
                        () -> new AuctionDoesNotExist("Auction with id '%s' not found".formatted(bid.getAuctionId()))
                );

        auction.placeNewBid(bid);

        return auctionRepository.save(auction);
    }

    @Transactional
    public void updateAuction(Auction auction) {
        Auction auctionToUpdate = auctionRepository
                .findById(auction.getId())
                .orElseThrow(
                        () -> new AuctionDoesNotExist("Auction with id '%s' not found".formatted(auction.getId()))
                );

        auctionToUpdate.applyChangesFrom(auction);

        auctionRepository.save(auctionToUpdate);
    }

    public List<Auction> getAllAuctionsByStatus(AuctionStatus status) {
        return auctionRepository.findAllByStatus(status);
    }

    public Auction getAuctionById(UUID id) {
        return auctionRepository
                .findById(id)
                .orElseThrow(
                        () -> new AuctionDoesNotExist("Auction with id '%s' not found".formatted(id))
                );
    }

    public Auction getAuctionByCarId(UUID carId) {
        return auctionRepository
                .findByCarId(carId)
                .orElseThrow(
                        () -> new AuctionDoesNotExist("Auction for a car with id '%s' not found".formatted(carId))
                );
    }

    public void updateAuctionStatus(AuctionStatus status, UUID auctionId) {
        auctionRepository.updateAuctionStatusById(status, auctionId);
    }
}

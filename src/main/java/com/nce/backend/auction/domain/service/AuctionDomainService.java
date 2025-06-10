package com.nce.backend.auction.domain.service;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AutoBid;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.repository.AuctionRepository;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.PaginatedResult;
import com.nce.backend.auction.exceptions.AuctionDoesNotExist;
import com.nce.backend.auction.exceptions.AuctionIllegalStateException;
import com.nce.backend.common.event.auction.AuctionRestartedEvent;
import com.nce.backend.common.event.auction.NewAuctionStartedEvent;
import com.nce.backend.common.event.auction.NewBidPlacedEvent;
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
            throw new AuctionIllegalStateException("Auction for car '%s' already exists".formatted(
                    auction.getCarDetails().getCarId())
            );
        }

        auction.setStatus(AuctionStatus.ACTIVE);
        Auction savedAuction = auctionRepository.save(auction);

        eventPublisher.publishEvent(
                new NewAuctionStartedEvent(
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

        final boolean isFirstTimeBid = auction
                .getBids()
                .stream()
                .noneMatch(b -> b.getBidderId().equals(bid.getBidderId()));

        auction.placeNewBid(bid);
        auction.triggerAutoBids();

        if (isFirstTimeBid) {
            eventPublisher.publishEvent(
                    new NewBidPlacedEvent(bid.getBidderId(), auction.getCarDetails().getCarId())
            );
        }

        return auctionRepository.save(auction);
    }

    @Transactional
    public Auction placeAutoBidOnAuction(AutoBid autoBid) {
        Auction auction = auctionRepository
                .findById(autoBid.getAuctionId())
                .orElseThrow(
                        () -> new AuctionDoesNotExist("Auction with id '%s' not found".formatted(autoBid.getAuctionId()))
                );

        final boolean isFirstTimeBid = auction
                .getBids()
                .stream()
                .noneMatch(b -> b.getBidderId().equals(autoBid.getBidderId()));

        auction.placeNewAutoBid(autoBid);
        auction.triggerAutoBids();

        if (isFirstTimeBid) {
            eventPublisher.publishEvent(
                    new NewBidPlacedEvent(autoBid.getBidderId(), auction.getCarDetails().getCarId())
            );
        }

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

    @Transactional
    public void updateRestartAuction(Auction auction) {
        Auction auctionToRestart = auctionRepository
                .findById(auction.getId())
                .orElseThrow(
                        () -> new AuctionDoesNotExist("Auction with id '%s' not found".formatted(auction.getId()))
                );

        auctionToRestart.restartWithNewData(auction);
        auctionRepository.save(auctionToRestart);

        eventPublisher.publishEvent(
                new AuctionRestartedEvent(
                        auctionToRestart.getCarDetails().getCarId(),
                        auctionToRestart.getId(),
                        auctionToRestart.getEndDateTime()
                )
        );
    }

    public PaginatedResult<Auction> getAllByStatus(AuctionStatus status, int page, int size) {
        return auctionRepository.findAllByStatus(status, page, size);
    }

    public PaginatedResult<Auction> getAllByCarIdsAndStatus(List<UUID> ids, AuctionStatus status, int page, int size) {
        return auctionRepository.findAllByCarIdsAndStatus(ids, status, page, size);
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

    @Transactional
    public void updateAuctionStatus(AuctionStatus status, UUID auctionId) {
        auctionRepository.updateAuctionStatusById(status, auctionId);
    }
}

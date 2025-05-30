package com.nce.backend.auction.domain.repository;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository {
    Optional<Auction> findById(UUID id);
    List<Auction> findAll();
    Auction save(Auction auction);
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByCarId(UUID carId);
    List<Auction> findAllByStatus(AuctionStatus status);
    Optional<Auction> findByCarId(UUID carId);
    void updateAuctionStatusById(AuctionStatus status, UUID auctionId);
    List<Auction> findAllByCarIdsAndStatus(List<UUID> ids, AuctionStatus status);
}

package com.nce.backend.auction.domain.repository;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.PaginatedResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository {
    Optional<Auction> findById(UUID id);
    PaginatedResult<Auction> findAll(int page, int size);
    Auction save(Auction auction);
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByCarId(UUID carId);
    PaginatedResult<Auction> findAllByStatus(AuctionStatus status, int page, int size);
    Optional<Auction> findByCarId(UUID carId);
    void updateAuctionStatusById(AuctionStatus status, UUID auctionId);
    PaginatedResult<Auction> findAllByCarIdsAndStatus(List<UUID> ids, AuctionStatus status, int page, int size);
}

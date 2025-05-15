package com.nce.backend.auction.domain.repository;


import com.nce.backend.auction.domain.valueObjects.Bid;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidRepository {
    Optional<Bid> findById(UUID id);
    List<Bid> findAllByAuctionId(UUID id);
    Bid save(Bid car);
    void deleteById(UUID id);
}

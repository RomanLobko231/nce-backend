package com.nce.backend.auction.infrastructure.jpa;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.repository.AuctionRepository;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepository {

    private final AuctionJpaRepository auctionRepository;
    private final AuctionJpaMapper mapper;

    @Override
    public Optional<Auction> findById(UUID id) {
        return auctionRepository
                .findById(id)
                .map(mapper::toDomainAuction);
    }

    @Override
    public List<Auction> findAll() {

        return auctionRepository
                .findAll()
                .stream()
                .map(mapper::toDomainAuction)
                .toList();
    }

    @Override
    public Auction save(Auction auction) {
        AuctionJpaEntity jpaEntity = mapper.toJpaAuction(auction);
        AuctionJpaEntity savedEntity = auctionRepository.save(jpaEntity);

        return mapper.toDomainAuction(savedEntity);
    }

    @Override
    public void deleteById(UUID id) {
        auctionRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return auctionRepository.existsById(id);
    }

    @Override
    public boolean existsByCarId(UUID carId) {
        return auctionRepository.existsByCarId(carId);
    }

    @Override
    public List<Auction> findAllByStatus(AuctionStatus status) {
        return auctionRepository
                .findAllByStatus(status.name())
                .stream()
                .map(mapper::toDomainAuction)
                .toList();
    }

    @Override
    public Optional<Auction> findByCarId(UUID carId) {
        return auctionRepository
                .findByCarId(carId)
                .map(mapper::toDomainAuction);
    }

    @Override
    public void updateAuctionStatusById(AuctionStatus status, UUID auctionId) {
        auctionRepository.updateAuctionStatusById(status.name(), auctionId);
    }

    @Override
    public List<Auction> findAllByCarIdsAndStatus(List<UUID> ids, AuctionStatus status) {
        return auctionRepository
                .findAllByCarIdsAndStatus(ids, status.name())
                .stream()
                .map(mapper::toDomainAuction)
                .toList();
    }
}

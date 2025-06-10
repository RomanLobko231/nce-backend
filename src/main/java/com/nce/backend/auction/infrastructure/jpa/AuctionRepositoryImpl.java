package com.nce.backend.auction.infrastructure.jpa;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.repository.AuctionRepository;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
import com.nce.backend.auction.domain.valueObjects.PaginatedResult;
import com.nce.backend.auction.infrastructure.jpa.entity.AuctionJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PaginatedResult<Auction> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuctionJpaEntity> result = auctionRepository.findAll(pageable);

        List<Auction> auctions = result
                .getContent()
                .stream()
                .map(mapper::toDomainAuction)
                .toList();

        return new PaginatedResult<>(
                auctions,
                result.getTotalPages(),
                result.getTotalElements(),
                result.getNumber()
        );
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
    public PaginatedResult<Auction> findAllByStatus(AuctionStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuctionJpaEntity> result = auctionRepository.findAllByStatus(status.name(), pageable);

        List<Auction> auctions = result
                .getContent()
                .stream()
                .map(mapper::toDomainAuction)
                .toList();

        return new PaginatedResult<>(
                auctions,
                result.getTotalPages(),
                result.getTotalElements(),
                result.getNumber()
        );
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
    public PaginatedResult<Auction> findAllByCarIdsAndStatus(List<UUID> ids, AuctionStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuctionJpaEntity> result = auctionRepository.findAllByCarIdsAndStatus(ids, status.name(), pageable);

        List<Auction> auctions = result
                .getContent()
                .stream()
                .map(mapper::toDomainAuction)
                .toList();

        return new PaginatedResult<>(
                auctions,
                result.getTotalPages(),
                result.getTotalElements(),
                result.getNumber()
        );

    }
}

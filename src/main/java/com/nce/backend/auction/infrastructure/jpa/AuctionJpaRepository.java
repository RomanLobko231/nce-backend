package com.nce.backend.auction.infrastructure.jpa;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionJpaRepository extends JpaRepository<AuctionJpaEntity, UUID> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM auction WHERE car_id = :carId)", nativeQuery = true)
    boolean existsByCarId(@Param(value = "carId") UUID carId);

    @Query(value = "SELECT * FROM auction WHERE status = :status", nativeQuery = true)
    List<AuctionJpaEntity> findAllByStatus(@Param(value = "status") String status);

    @Query(value = "SELECT * FROM auction WHERE car_id = :carId", nativeQuery = true)
    Optional<AuctionJpaEntity> findByCarId(@Param(value = "carId") UUID carId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE auction SET status = :status WHERE id = :auctionId", nativeQuery = true)
    void updateAuctionStatusById(@Param("status") String status, @Param("auctionId") UUID auctionId);

    @Query(value = "SELECT * FROM auction WHERE car_id IN (:ids) AND status = :status", nativeQuery = true)
    List<AuctionJpaEntity> findAllByCarIdsAndStatus(@Param("ids") List<UUID> ids, @Param("status") String status);
}

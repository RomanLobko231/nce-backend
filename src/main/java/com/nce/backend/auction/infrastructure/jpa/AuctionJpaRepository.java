package com.nce.backend.auction.infrastructure.jpa;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AuctionStatus;
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

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE auction
            SET starting_price = :startingPrice, expected_price = :expectedPrice, minimal_step = :minimalStep, end_date_time = :endDateTime
            WHERE id = :auctionId AND status != 'ACTIVE'
            """, nativeQuery = true)
    int updateAuctionSpecificFields(
            @Param("startingPrice") BigDecimal startingPrice,
            @Param("expectedPrice") BigDecimal expectedPrice,
            @Param("minimalStep") BigDecimal minimalStep,
            @Param("endDateTime") Instant endDateTime,
            @Param("auctionId") UUID auctionId
    );
}

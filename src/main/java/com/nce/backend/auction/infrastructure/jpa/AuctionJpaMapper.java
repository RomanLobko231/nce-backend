package com.nce.backend.auction.infrastructure.jpa;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.AutoBid;
import com.nce.backend.auction.domain.valueObjects.Bid;
import com.nce.backend.auction.domain.valueObjects.CarDetails;
import com.nce.backend.auction.infrastructure.jpa.entity.AuctionJpaEntity;
import com.nce.backend.auction.infrastructure.jpa.entity.AutoBidEmbeddable;
import com.nce.backend.auction.infrastructure.jpa.entity.BidEmbeddable;
import com.nce.backend.auction.infrastructure.jpa.entity.CarDetailsEmbeddable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class AuctionJpaMapper {

    public Auction toDomainAuction(AuctionJpaEntity jpaEntity) {
        return Auction
                .builder()
                .id(jpaEntity.getId())
                .bids(
                        jpaEntity
                                .getBids()
                                .stream()
                                .map(bidEmbeddable -> Bid
                                        .builder()
                                        .auctionId(jpaEntity.getId())
                                        .amount(bidEmbeddable.getAmount())
                                        .placedAt(bidEmbeddable.getPlacedAt())
                                        .bidderId(bidEmbeddable.getBidderId())
                                        .build())
                                .collect(Collectors.toCollection(ArrayList::new))
                )
                .autoBids(
                        jpaEntity
                                .getAutoBids()
                                .stream()
                                .map(autoBidEmbeddable -> AutoBid
                                        .builder()
                                        .auctionId(jpaEntity.getId())
                                        .limitAmount(autoBidEmbeddable.getLimitAmount())
                                        .placedAt(autoBidEmbeddable.getPlacedAt())
                                        .bidderId(autoBidEmbeddable.getBidderId())
                                        .build())
                                .collect(Collectors.toCollection(ArrayList::new))
                )
                .expectedPrice(jpaEntity.getExpectedPrice())
                .status(jpaEntity.getStatus())
                .endDateTime(jpaEntity.getEndDateTime())
                .minimalStep(jpaEntity.getMinimalStep())
                .startingPrice(jpaEntity.getStartingPrice())
                .highestBid(
                        this.toDomainBid(jpaEntity)
                )
                .carDetails(
                        this.toDomainDetails(jpaEntity.getCarDetails())
                )
                .build();
    }

    public AuctionJpaEntity toJpaAuction(Auction auction) {
        return AuctionJpaEntity
                .builder()
                .id(auction.getId())
                .bids(
                        auction
                                .getBids()
                                .stream()
                                .map(this::toJpaBid)
                                .collect(Collectors.toCollection(ArrayList::new))
                )
                .autoBids(
                        auction
                                .getAutoBids()
                                .stream()
                                .map(this::toJpaAutoBid)
                                .collect(Collectors.toCollection(ArrayList::new))
                )
                .expectedPrice(auction.getExpectedPrice())
                .status(auction.getStatus())
                .endDateTime(auction.getEndDateTime())
                .minimalStep(auction.getMinimalStep())
                .startingPrice(auction.getStartingPrice())
                .highestBid(
                        this.toJpaBid(auction.getHighestBid())
                )
                .carDetails(
                        this.toJpaDetails(auction.getCarDetails())
                )
                .build();
    }

    private BidEmbeddable toJpaBid(Bid domainBid) {
        return domainBid == null ? null : BidEmbeddable
                .builder()
                .amount(domainBid.getAmount())
                .bidderId(domainBid.getBidderId())
                .placedAt(domainBid.getPlacedAt())
                .build();
    }

    private AutoBidEmbeddable toJpaAutoBid(AutoBid domainAutoBid) {
        return AutoBidEmbeddable
                .builder()
                .limitAmount(domainAutoBid.getLimitAmount())
                .bidderId(domainAutoBid.getBidderId())
                .placedAt(domainAutoBid.getPlacedAt())
                .build();
    }

    private Bid toDomainBid(AuctionJpaEntity jpaEntity) {
        return jpaEntity.getHighestBid() == null ? null : Bid
                .builder()
                .auctionId(jpaEntity.getId())
                .amount(jpaEntity.getHighestBid().getAmount())
                .placedAt(jpaEntity.getHighestBid().getPlacedAt())
                .bidderId(jpaEntity.getHighestBid().getBidderId())
                .build();
    }

    private CarDetails toDomainDetails(CarDetailsEmbeddable jpaDetails) {
        return CarDetails
                .builder()
                .carId(jpaDetails.getCarId())
                .modelYear(jpaDetails.getModelYear())
                .thumbnailImageUrl(jpaDetails.getThumbnailImageUrl())
                .makeModel(jpaDetails.getMakeModel())
                .build();
    }

    private CarDetailsEmbeddable toJpaDetails(CarDetails domainDetails) {
        return CarDetailsEmbeddable
                .builder()
                .carId(domainDetails.getCarId())
                .modelYear(domainDetails.getModelYear())
                .thumbnailImageUrl(domainDetails.getThumbnailImageUrl())
                .makeModel(domainDetails.getMakeModel())
                .build();
    }
}

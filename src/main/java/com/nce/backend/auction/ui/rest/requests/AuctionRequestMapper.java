package com.nce.backend.auction.ui.rest.requests;

import com.nce.backend.auction.domain.entities.Auction;
import com.nce.backend.auction.domain.valueObjects.CarDetails;
import org.springframework.stereotype.Service;

@Service
public class AuctionRequestMapper {

    public Auction toDomainFromNew(NewAuctionRequest request){
        return Auction
                .builder()
                .endDateTime(request.endDateTime())
                .startingPrice(request.startingPrice())
                .expectedPrice(request.expectedPrice())
                .minimalStep(request.minimalStep())
                .carDetails(
                        CarDetails
                                .builder()
                                .carId(request.carId())
                                .makeModel(request.makeModel())
                                .modelYear(request.modelYear())
                                .thumbnailImageUrl(request.thumbnailImageUrl())
                                .build()
                )
                .build();
    }

    public Auction toDomainFromUpdate(UpdateAuctionRequest request){
        return Auction
                .builder()
                .endDateTime(request.endDateTime())
                .startingPrice(request.startingPrice())
                .expectedPrice(request.expectedPrice())
                .minimalStep(request.minimalStep())
                .id(request.id())
                .build();
    }
}

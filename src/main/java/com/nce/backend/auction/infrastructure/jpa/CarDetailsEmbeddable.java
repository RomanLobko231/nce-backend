package com.nce.backend.auction.infrastructure.jpa;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CarDetailsEmbeddable {

    @Column(name = "car_id", nullable = false)
    private UUID carId;

    @Column(name = "thumbnail_image_url", nullable = false)
    private String thumbnailImageUrl;

    @Column(name = "make_model", nullable = false)
    private String makeModel;

    @Column(name = "model_year", nullable = false)
    private String modelYear;

}

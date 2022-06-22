package com.devmem.mileagebackend.feature.mileage.domain;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_review_by_place")
public class ReviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewByPlaceId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID placeId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    private Boolean isFirstReview;

    @Builder
    public ReviewHistory(UUID placeId, UUID reviewId, Boolean isFirstReview){
        this.placeId = placeId;
        this.reviewId = reviewId;
        this.isFirstReview = isFirstReview;
    }
}

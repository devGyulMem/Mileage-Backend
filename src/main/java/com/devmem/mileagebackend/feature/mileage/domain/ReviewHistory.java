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
@Table(name = "tb_review_history")
public class ReviewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer reviewHistoryId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID placeId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    private Boolean isFirstReview;

    @Builder
    public ReviewHistory(UUID placeId, UUID reviewId, UUID userId, Boolean isFirstReview){
        this.placeId = placeId;
        this.reviewId = reviewId;
        this.userId = userId;
        this.isFirstReview = isFirstReview;
    }
}

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
@Table(name = "tb_mileage_user")
public class ReviewByPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rPId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID placeId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    @Builder
    public ReviewByPlace(UUID placeId, UUID reviewId){
        this.placeId = placeId;
        this.reviewId = reviewId;
    }
}

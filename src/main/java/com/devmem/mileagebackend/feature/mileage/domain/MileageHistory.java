package com.devmem.mileagebackend.feature.mileage.domain;

import com.devmem.mileagebackend.utils.BastTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_mileage_history")
public class MileageHistory extends BastTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID historyId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID placeId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    private String action;

    private String content;

    private long points;

    @Builder
    public MileageHistory(UUID userId, UUID placeId, UUID reviewId, String action, String content, long points){
        this.userId = userId;
        this.placeId = placeId;
        this.reviewId = reviewId;
        this.action = action;
        this.content = content;
        this.points = points;
    }
}

package com.devmem.mileagebackend.feature.mileage.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_mileage_user")
public class MileageUser {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID mileageUserId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    private long mileage;

    @Builder
    public MileageUser(UUID userId, long mileage){
        this.userId = userId;
        this.mileage = mileage;
    }
}

package com.devmem.mileagebackend.feature.mileage.persistence;

import com.devmem.mileagebackend.feature.mileage.domain.MileageHistory;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageHistoryRepo extends JpaRepository<MileageHistory, Integer> {
    Optional<MileageHistory> findFirstByReviewIdOrderByRegDtmDesc(UUID reviewId);
}

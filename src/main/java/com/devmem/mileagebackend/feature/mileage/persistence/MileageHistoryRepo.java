package com.devmem.mileagebackend.feature.mileage.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmem.mileagebackend.feature.mileage.domain.MileageHistory;

public interface MileageHistoryRepo extends JpaRepository<MileageHistory, Integer> {
    Optional<MileageHistory> findFirstByReviewIdOrderByMileageHistoryIdDesc(UUID reviewId);
    List<MileageHistory> findAllByUserId(UUID userId);
}

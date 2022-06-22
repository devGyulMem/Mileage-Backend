package com.devmem.mileagebackend.feature.mileage.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmem.mileagebackend.feature.mileage.domain.ReviewHistory;

public interface ReviewHistoryRepo extends JpaRepository<ReviewHistory, Integer> {
    Optional<ReviewHistory> findByReviewId(UUID reviewId);
    List<ReviewHistory> findAllByPlaceId(UUID placeId);
    Optional<ReviewHistory> findByPlaceIdAndUserId(UUID placeId, UUID userId);
}

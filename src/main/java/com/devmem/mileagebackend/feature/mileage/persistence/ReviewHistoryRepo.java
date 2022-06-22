package com.devmem.mileagebackend.feature.mileage.persistence;

import com.devmem.mileagebackend.feature.mileage.domain.ReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewHistoryRepo extends JpaRepository<ReviewHistory, Integer> {
    ReviewHistory findByReviewId(UUID reviewId);
    List<ReviewHistory> findAllByPlaceId(UUID placeId);
    ReviewHistory deleteReviewHistoryByReviewId(UUID reviewId);
}

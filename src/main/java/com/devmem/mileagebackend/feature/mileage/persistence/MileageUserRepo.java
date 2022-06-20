package com.devmem.mileagebackend.feature.mileage.persistence;

import com.devmem.mileagebackend.feature.mileage.domain.MileageUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MileageUserRepo extends JpaRepository<MileageUser, Integer> {
    Optional<MileageUser> findByUserId(UUID userId);
}

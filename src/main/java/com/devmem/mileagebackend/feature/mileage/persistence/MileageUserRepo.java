package com.devmem.mileagebackend.feature.mileage.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmem.mileagebackend.feature.mileage.domain.MileageUser;

public interface MileageUserRepo extends JpaRepository<MileageUser, Integer> {
    Optional<MileageUser> findByUserId(UUID userId);
}

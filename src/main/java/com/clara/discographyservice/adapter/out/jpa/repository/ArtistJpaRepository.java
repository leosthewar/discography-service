package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.adapter.out.jpa.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistJpaRepository  extends JpaRepository<ArtistEntity, Long> {
    Optional<ArtistEntity> findByDiscogsId(Long discogsId);
}

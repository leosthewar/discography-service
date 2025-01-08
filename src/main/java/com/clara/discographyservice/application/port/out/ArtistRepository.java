package com.clara.discographyservice.application.port.out;

import com.clara.discographyservice.application.domain.model.artist.ArtistEntity;

import java.util.Optional;

public interface ArtistRepository {
    ArtistEntity save(ArtistEntity artistEntity);

    Optional<ArtistEntity> findById(Long id);

    Optional<ArtistEntity> findByDiscogsArtistId(Long discogsArtistId);
}

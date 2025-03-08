package com.leosdev.discographyservice.application.port.out;

import com.leosdev.discographyservice.application.domain.model.artist.ArtistDiscographySummary;
import com.leosdev.discographyservice.application.domain.model.artist.ArtistEntity;

import java.util.List;
import java.util.Optional;

public interface ArtistRepository {
    ArtistEntity save(ArtistEntity artistEntity);

    Optional<ArtistEntity> findById(Long id);

    Optional<ArtistEntity> findByDiscogsArtistId(Long discogsArtistId);

    List<ArtistDiscographySummary> compareDiscographiesSummary(List<Long> discogsArtistIds);
    
}

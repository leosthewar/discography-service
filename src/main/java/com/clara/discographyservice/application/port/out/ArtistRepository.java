package com.clara.discographyservice.application.port.out;

import com.clara.discographyservice.application.domain.model.Artist;

import java.util.Optional;

public interface ArtistRepository {
    Artist save(Artist artist);

    Optional<Artist> findByDiscogsArtistId(Long discogsArtistId);
}

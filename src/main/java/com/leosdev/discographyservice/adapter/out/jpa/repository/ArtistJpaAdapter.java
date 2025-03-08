package com.leosdev.discographyservice.adapter.out.jpa.repository;

import com.leosdev.discographyservice.application.domain.model.artist.ArtistDiscographySummary;
import com.leosdev.discographyservice.application.domain.model.artist.ArtistEntity;
import com.leosdev.discographyservice.application.port.out.ArtistRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class ArtistJpaAdapter implements ArtistRepository {

    private final ArtistJpaRepository artistJpaRepository;

    public ArtistJpaAdapter(ArtistJpaRepository artistJpaRepository) {
        this.artistJpaRepository = artistJpaRepository;
    }

    @Override
    public ArtistEntity save(ArtistEntity artist) {
       return  artistJpaRepository.save(artist);
    }

    @Override
    public Optional<ArtistEntity> findById(Long id) {
        return artistJpaRepository.findById(id);
    }

    @Override
    public Optional<ArtistEntity> findByDiscogsArtistId(Long discogsArtistId) {
        return artistJpaRepository.findByDiscogsId(discogsArtistId);
    }

    @Override
    public List<ArtistDiscographySummary> compareDiscographiesSummary(List<Long> discogsArtistIds) {
        return artistJpaRepository.compareDiscographiesSummary(discogsArtistIds);
    }
}

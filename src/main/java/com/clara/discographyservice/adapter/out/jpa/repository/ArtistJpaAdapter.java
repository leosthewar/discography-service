package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.adapter.out.jpa.entity.ArtistEntity;
import com.clara.discographyservice.adapter.out.jpa.entity.ArtistEntityMapper;
import com.clara.discographyservice.application.domain.model.Artist;
import com.clara.discographyservice.application.port.out.ArtistRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ArtistJpaAdapter implements ArtistRepository {

    private final ArtistJpaRepository artistJpaRepository;

    public ArtistJpaAdapter(ArtistJpaRepository artistJpaRepository) {
        this.artistJpaRepository = artistJpaRepository;
    }

    @Override
    public Artist save(Artist artist) {
        ArtistEntity artistEntity = ArtistEntityMapper.toEntity(artist);
       return  ArtistEntityMapper.toDomain(artistJpaRepository.save(artistEntity));
    }

    @Override
    public Optional<Artist> findByDiscogsArtistId(Long discogsArtistId) {
        return artistJpaRepository.findByDiscogsId(discogsArtistId).map(ArtistEntityMapper::toDomain);
    }
}

package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.application.domain.model.artist.ImportDetailsProjection;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportEntity;
import com.clara.discographyservice.application.port.out.ArtistDiscographyImportRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
class ArtistDiscographyImportJpaAdapter implements ArtistDiscographyImportRepository {

    private final ArtistDiscographyImportJpaRepository artistDiscographyImportJpaRepository;

    public ArtistDiscographyImportJpaAdapter(ArtistDiscographyImportJpaRepository artistDiscographyImportJpaRepository) {
        this.artistDiscographyImportJpaRepository = artistDiscographyImportJpaRepository;
    }


    @Override
    public ArtistDiscographyImportEntity save(ArtistDiscographyImportEntity artistDiscographyImportEntity) {
        return artistDiscographyImportJpaRepository.save(artistDiscographyImportEntity);
    }

    @Override
    public Optional<ArtistDiscographyImportEntity> findById(Long importId) {
        return  artistDiscographyImportJpaRepository.findById(importId);
    }


    @Override
    public Optional<ArtistDiscographyImportEntity> findByDiscogsArtistId(Long discogsArtistId) {
        return artistDiscographyImportJpaRepository.findByDiscogsArtistId(discogsArtistId);
    }

    @Override
    public List<ImportDetailsProjection> findNotImportedReleases() {
        Pageable pageable = PageRequest.of(0, 50);
        return artistDiscographyImportJpaRepository.findNotImportedReleases(pageable);
    }

    @Override
    public void setImportDetailTrue(Long detailId) {

        artistDiscographyImportJpaRepository.updateImportDetailToTrue(detailId);
    }

    @Override
    public void deleteImportDetail(Long detailId) {
        artistDiscographyImportJpaRepository.deleteImportDetail(detailId);
    }

    @Override
    public Integer countReleasesNotImportedByArtistId(Long importId) {
        return artistDiscographyImportJpaRepository.countReleasesNotImportedByArtistId(importId);
    }


}

package com.clara.discographyservice.application.port.out;

import com.clara.discographyservice.application.domain.model.artist.ImportDetailsProjection;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportEntity;

import java.util.List;
import java.util.Optional;


public interface ArtistDiscographyImportRepository {


    ArtistDiscographyImportEntity save(ArtistDiscographyImportEntity artistDiscographyImportEntity);

    Optional<ArtistDiscographyImportEntity> findById(Long importId);

    Optional<ArtistDiscographyImportEntity> findByDiscogsArtistId(Long discogsArtistId);

    List<ImportDetailsProjection> findNotImportedReleases();

    void  setImportDetailTrue(Long detailId);

    void deleteImportDetail(Long detailId);

    Integer countReleasesNotImportedByArtistId(Long importId);

}

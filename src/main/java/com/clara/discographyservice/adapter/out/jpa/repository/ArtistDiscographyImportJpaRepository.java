package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.application.domain.model.artist.ImportDetailsProjection;
import com.clara.discographyservice.application.domain.model.discographyimport.ArtistDiscographyImportEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

interface ArtistDiscographyImportJpaRepository extends JpaRepository<ArtistDiscographyImportEntity, Long> {

    Optional<ArtistDiscographyImportEntity> findByDiscogsArtistId(Long discogsArtistId);

    @Query("""
           SELECT
           adid.id as importDetailId,
           adi.id AS importId,
           adi.discogsArtistId AS discogsArtistId,
           adid.discogsReleaseId AS discogsReleaseId
           FROM ArtistDiscographyImportEntity adi
           JOIN adi.details adid
           WHERE adid.imported = false
           ORDER BY adid.id asc
           """)
    List<ImportDetailsProjection> findNotImportedReleases(Pageable pageable);

    @Query("""
            SELECT COUNT(adid.id)
            FROM ArtistDiscographyImportEntity adi
            JOIN adi.details adid
            WHERE adi.id = :importId AND adid.imported = false""")
    Integer countReleasesNotImportedByArtistId(Long importId);

    @Modifying
    @Transactional
    @Query("UPDATE ArtistDiscographyImportDetailEntity d SET d.imported = true WHERE d.id = :detailId")
    void updateImportDetailToTrue(Long detailId);

    @Modifying
    @Transactional
    @Query("DELETE  ArtistDiscographyImportDetailEntity d  WHERE d.id = :detailId")
    void deleteImportDetail(Long detailId);

}

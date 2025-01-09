package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ReleaseJpaRepository extends JpaRepository<ReleaseEntity, Long> {

    boolean existsByDiscogsId(Long discogsId);


    @Query(value = """
    SELECT r.id, r.discogs_id, r.status, r."year", r.discogs_resource_url, r.discogs_uri, r.format_quantity, r.title, r.country, r.released, r.notes, r.discogs_thumb, r.estimated_weight, r.blocked_from_sale
    FROM artist_discography_import adi
    JOIN artist_discography_import_detail adid ON adi.id = adid.artist_import_id
    JOIN release r ON r.discogs_id = adid.discogs_release_id
    WHERE adi.discogs_artist_id = :discogsArtistId
      AND adi.status = 'COMPLETED'
    """, nativeQuery = true)
    Page<ReleaseEntity> findReleasesByDiscogsArtistId(@Param("discogsArtistId") Long discogsArtistId, Pageable pageable);



}

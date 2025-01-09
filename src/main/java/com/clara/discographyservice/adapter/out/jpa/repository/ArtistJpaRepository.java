package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.application.domain.model.artist.ArtistDiscographySummary;
import com.clara.discographyservice.application.domain.model.artist.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface ArtistJpaRepository  extends JpaRepository<ArtistEntity, Long> {
    Optional<ArtistEntity> findByDiscogsId(Long discogsId);


    @Query(value = """
        SELECT
            adi.discogs_artist_id,
            COUNT(DISTINCT r.id) AS num_releases,
            MIN(r.year) AS first_release_year,
            MAX(r.year) AS last_release_year,
            STRING_AGG(DISTINCT rg.name, '##') FILTER (WHERE rg.name IS NOT NULL) AS genres,
            STRING_AGG(DISTINCT rs.name, '##') FILTER (WHERE rs.name IS NOT NULL) AS styles
        FROM artist_discography_import adi
        JOIN artist_discography_import_detail adid ON adid.artist_import_id = adi.id 
        JOIN "release" r ON r.discogs_id = adid.discogs_release_id 
        LEFT JOIN release_genre rg ON r.id = rg.release_id
        LEFT JOIN release_style rs ON r.id = rs.release_id
        WHERE adi.discogs_artist_id IN (:discogsArtistId) 
          AND adi.status = 'COMPLETED' 
          AND r."year" != 0	
        GROUP BY adi.discogs_artist_id;
    """ , nativeQuery = true)
    List<ArtistDiscographySummary> compareDiscographiesSummary(@Param("discogsArtistId") List<Long> discogsArtistIds);
}

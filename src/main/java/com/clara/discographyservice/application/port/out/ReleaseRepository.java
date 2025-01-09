package com.clara.discographyservice.application.port.out;

import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository {
    ReleaseEntity save(ReleaseEntity release);

    boolean existsReleaseByDiscogsId(Long discogsReleaseId);

    Page<ReleaseEntity> findReleasesByDiscogsArtistIdSortByYear(Long discogsArtistId, Pageable pageable);
}

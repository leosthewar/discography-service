package com.clara.discographyservice.adapter.out.jpa.repository;

import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import com.clara.discographyservice.application.port.out.ReleaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
class ReleaseJpaAdapter implements ReleaseRepository {

    private final ReleaseJpaRepository releaseJpaRepository;

    public ReleaseJpaAdapter(ReleaseJpaRepository releaseJpaRepository) {
        this.releaseJpaRepository = releaseJpaRepository;
    }

    @Override
    public ReleaseEntity save(ReleaseEntity releaseEntity) {
       return  releaseJpaRepository.save(releaseEntity);
    }

    @Override
    public boolean existsReleaseByDiscogsId(Long discogsReleaseId) {
        return releaseJpaRepository.existsByDiscogsId(discogsReleaseId);
    }


    public Page<ReleaseEntity> findReleasesByDiscogsArtistIdSortByYear(Long discogsArtistId, Pageable pageable) {
        return releaseJpaRepository.findReleasesByDiscogsArtistId(discogsArtistId, pageable);
    }

}

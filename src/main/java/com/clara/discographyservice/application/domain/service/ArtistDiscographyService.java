package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.domain.model.artist.ArtistEntity;
import com.clara.discographyservice.application.domain.model.release.ReleaseEntity;
import com.clara.discographyservice.application.domain.model.release.ReleaseEntityMapper;
import com.clara.discographyservice.application.port.in.ArtistDiscographyUseCase;
import com.clara.discographyservice.application.port.in.model.GetArtistDiscographyResponse;
import com.clara.discographyservice.application.port.in.model.GetDiscographyByDiscogsArtistIdCommand;
import com.clara.discographyservice.application.port.out.ArtistRepository;
import com.clara.discographyservice.application.port.out.ReleaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class ArtistDiscographyService implements ArtistDiscographyUseCase {

    private final ReleaseRepository releaseRepository;
    private final ArtistRepository artistRepository;

    public ArtistDiscographyService(ReleaseRepository releaseRepository,
                                    ArtistRepository artistRepository) {
        this.releaseRepository = releaseRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public GetArtistDiscographyResponse findDiscographyByArtistIdSortByYear(GetDiscographyByDiscogsArtistIdCommand command){

        Optional<ArtistEntity> artist =  artistRepository.findByDiscogsArtistId(command.discogsArtistId());
        if (artist.isEmpty()) {
            return new GetArtistDiscographyResponse(null, "Artist not found in database", true);
        }
        if (!artist.get().getDiscographyImported()) {
            return new GetArtistDiscographyResponse(null, "Artist found but discography not imported", true);
        }
        Sort.Direction direction = Sort.Direction.fromString(command.sortDirection());
        Pageable pageable = PageRequest.of(command.page(), command.size(), Sort.by(direction, command.sort()));

        // Fetch paginated entities
        Page<ReleaseEntity> releaseEntities = releaseRepository.findReleasesByDiscogsArtistIdSortByYear(command.discogsArtistId(), pageable);

        // Map entities to response models
        return new GetArtistDiscographyResponse(releaseEntities.map(ReleaseEntityMapper::toResModel), "", false);
    }
}

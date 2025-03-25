package com.leosdev.discographyservice.application.domain.service;

import com.leosdev.discographyservice.application.domain.model.artist.ArtistDiscographySummary;
import com.leosdev.discographyservice.application.domain.model.artist.ArtistEntity;
import com.leosdev.discographyservice.application.domain.model.artist.responsemodel.ArtistDiscographySummaryResModel;
import com.leosdev.discographyservice.application.port.in.CompareArtistDiscographiesUseCase;
import com.leosdev.discographyservice.application.port.in.model.CompareDiscographiesByDiscogsAristIdCommand;
import com.leosdev.discographyservice.application.port.in.model.CompareDiscographiesResponse;
import com.leosdev.discographyservice.application.port.out.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompareArtistDiscographiesService implements CompareArtistDiscographiesUseCase {

    private final ArtistRepository artistRepository;

    public CompareArtistDiscographiesService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public CompareDiscographiesResponse compareDicographiesByDiscogsArtistId(CompareDiscographiesByDiscogsAristIdCommand command) {
        List<Optional<ArtistEntity>> artistEntities = command.discogsArtistIds().stream()
                                                             .map(artistRepository::findByDiscogsArtistId)
                                                             .toList();

        if (artistEntities.stream().anyMatch(Optional::isEmpty)) {
            return createErrorResponse("All artists must be present in the database");
        }

        if (artistEntities.stream().flatMap(Optional::stream).anyMatch(artist -> !artist.getDiscographyImported())) {
            return createErrorResponse("All artists must be imported");
        }

        List<ArtistDiscographySummaryResModel> discographySummaries = artistRepository.compareDiscographiesSummary(command.discogsArtistIds().stream().toList())
                                                                                      .stream()
                                                                                      .map(this::mapToResModel)
                                                                                      .collect(Collectors.toList());

        return new CompareDiscographiesResponse(discographySummaries, null, false);
    }

    private CompareDiscographiesResponse createErrorResponse(String errorMessage) {
        return new CompareDiscographiesResponse(null, errorMessage, true);
    }

    private ArtistDiscographySummaryResModel mapToResModel(ArtistDiscographySummary summary) {
        return new ArtistDiscographySummaryResModel(
                summary.getDiscogsArtistId(),
                summary.getNumReleases(),
                summary.getFirstReleaseYear(),
                summary.getLastReleaseYear(),
                summary.getLastReleaseYear()- summary.getFirstReleaseYear(),
                splitAndConvertToList(summary.getStyles()),
                splitAndConvertToList(summary.getGenres())
        );
    }

    private List<String> splitAndConvertToList(String data) {
        return List.of(data.split("##"));
    }
}

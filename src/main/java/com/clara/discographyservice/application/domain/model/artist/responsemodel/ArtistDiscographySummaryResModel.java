package com.clara.discographyservice.application.domain.model.artist.responsemodel;


import java.util.List;

public record ArtistDiscographySummaryResModel(
        Long discogsArtistId,
        Long numReleases,
        Integer firstReleaseYear,
        Integer lastReleaseYear,
        Integer activeYears,
        List<String> genres,
        List<String> styles
) {

}

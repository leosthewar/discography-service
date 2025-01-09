package com.clara.discographyservice.application.domain.model.artist;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ArtistDiscographySummary {

    private  Long discogsArtistId;
    private  Long numReleases;
    private  Integer firstReleaseYear;
    private  Integer lastReleaseYear;
    private  String  genres;
    private  String  styles;


    public ArtistDiscographySummary(
            Long discogsArtistId,
            Long numReleases,
            Integer firstReleaseYear,
            Integer lastReleaseYear,
            String   genres,
            String    styles
    ) {
        this.discogsArtistId = discogsArtistId;
        this.numReleases = numReleases;
        this.firstReleaseYear = firstReleaseYear;
        this.lastReleaseYear = lastReleaseYear;
        this.genres = genres;
        this.styles = styles;
    }

}

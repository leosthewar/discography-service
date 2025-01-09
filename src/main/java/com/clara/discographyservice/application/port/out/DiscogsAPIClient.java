package com.clara.discographyservice.application.port.out;

import com.clara.discographyservice.application.port.in.model.ArtistGetQuery;
import com.clara.discographyservice.application.port.in.model.ArtistReleasesGetQuery;
import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.clara.discographyservice.application.port.in.model.ReleaseGetQuery;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface DiscogsAPIClient {

    /*
     * @param artistQuery the object containing the query parameters
     * @Return a JsonNode containing the search results from Discogs API
     * @throws DiscogsException when Discogs API returns an error or occurs an unexpected error processing the response
     */
    @Nonnull
    JsonNode searchArtist(ArtistSearchQuery artistQuery);

    Optional<JsonNode> getArtist(ArtistGetQuery artistGetQuery);

    Optional<JsonNode> getArtistReleases(ArtistReleasesGetQuery artistReleasesGetQuery);

    Optional<JsonNode> getRelease(ReleaseGetQuery releaseGetQuery);

}

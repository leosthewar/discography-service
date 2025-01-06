package com.clara.discographyservice.application.port.out;

import com.clara.discographyservice.application.port.in.ArtistSearchQuery;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Nonnull;

public interface DiscogsAPIClient {

    /*
     * @param artistQuery the object containing the query parameters
     * @Return a JsonNode containing the search results from Discogs API
     * @throws DiscogsException when Discogs API returns an error or occurs an unexpected error processing the response
     */
    @Nonnull
    JsonNode searchArtist(ArtistSearchQuery artistQuery);

}

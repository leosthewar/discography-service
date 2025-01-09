package com.clara.discographyservice.application.port.in;

import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.clara.discographyservice.application.port.out.DiscogsException;
import com.fasterxml.jackson.databind.JsonNode;


public interface SearchArtistFromDiscogsUseCase {
    /**
     * Searches for an artist using the Discogs API.
     *
     * @param artistSearchQuery the object containing the query parameters
     * @return a JsonNode containing the search results from Discogs API
     * @throws DiscogsException when Discogs API returns an error or occurs an unexpected error processing the response
     */
    JsonNode searchArtist(ArtistSearchQuery artistSearchQuery) throws DiscogsException;
}

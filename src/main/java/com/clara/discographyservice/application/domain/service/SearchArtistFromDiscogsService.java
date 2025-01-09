package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.port.in.SearchArtistFromDiscogsUseCase;
import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
class SearchArtistFromDiscogsService implements SearchArtistFromDiscogsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SearchArtistFromDiscogsService.class);
    private final DiscogsAPIClient discogsAPIClient;


    public SearchArtistFromDiscogsService(DiscogsAPIClient discogsAPIClient) {
        this.discogsAPIClient = discogsAPIClient;
    }

    /**
     * Searches for an artist using the Discogs API.
     *
     * @see SearchArtistFromDiscogsUseCase#searchArtist
     */
    @Override
    public JsonNode searchArtist(ArtistSearchQuery artistSearchQuery) {
        logger.debug("Searching for artist with query: {}", artistSearchQuery);
        JsonNode rootNode = discogsAPIClient.searchArtist(artistSearchQuery);
        // Modify the pagination URLs
        ObjectNode urlsNode = (ObjectNode) rootNode.get("pagination")
                                                   .get("urls");
        if (urlsNode != null) {
            updatePaginationUrl(urlsNode, "next", artistSearchQuery, artistSearchQuery.page() + 1);
            updatePaginationUrl(urlsNode, "prev", artistSearchQuery, artistSearchQuery.page() - 1);
            updatePaginationUrl(urlsNode, "first", artistSearchQuery, 1);
            updatePaginationUrl(urlsNode, "last", artistSearchQuery, rootNode.get("pagination")
                                                                             .get("pages")
                                                                             .asInt());
        }
        return rootNode;

    }

    private void updatePaginationUrl(ObjectNode urlsNode, String key, ArtistSearchQuery query, int page) {
        if (urlsNode.get(key) != null) {
            String updatedUrl = new StringBuilder()
                    .append(query.currentURI())
                    .append("?q=")
                    .append(query.query())
                    .append("&page=")
                    .append(page)
                    .append("&per_page=")
                    .append(query.perPage())
                    .toString();
            urlsNode.put(key, updatedUrl);
        }
    }
}

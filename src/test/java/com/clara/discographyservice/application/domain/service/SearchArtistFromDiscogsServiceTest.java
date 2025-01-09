package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.clara.discographyservice.application.port.out.DiscogsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

class SearchArtistFromDiscogsServiceTest {

    private final DiscogsAPIClient discogsAPIClient = Mockito.mock(DiscogsAPIClient.class);

    private final SearchArtistFromDiscogsService searchArtistFromDiscogsService = new SearchArtistFromDiscogsService(discogsAPIClient);



    @Test
    void searchArtist_WhenArtistExists() throws JsonProcessingException {
        // Given - Build the ArtistSearchQuery and mock the DiscogsAPIClient response

        String currentURI= "http://domain.com/v1/discogs/artists/search";
        Integer page = 2;
        Integer perPage = 50;
        ArtistSearchQuery artistSearchQuery = new ArtistSearchQuery("Nirvana", currentURI, page, perPage);
        JsonNode jsonNode = getJsonNode("exists");
        when(discogsAPIClient.searchArtist(any())).thenReturn(jsonNode);

        // When - Call the searchArtist method
        JsonNode result = searchArtistFromDiscogsService.searchArtist(artistSearchQuery);

        // Then - Verify that the result is correct
        // Assert that the result is present
        assertNotNull(result);
        JsonNode results = result.get("results");
        // validate results is not null
        assertNotNull(results);
        // validate results is not empty
        assertTrue(results.isArray() && !results.isEmpty());
        String urlNext = result.get("pagination").get("urls").get("next").asText();
        String urlPrev = result.get("pagination").get("urls").get("prev").asText();
        String urlFirst = result.get("pagination").get("urls").get("first").asText();
        String urlLast = result.get("pagination").get("urls").get("last").asText();

        // validate urls changed  as expected
        assertEquals(currentURI+"?q="+artistSearchQuery.query()+"&page="+(artistSearchQuery.page()+1)+"&per_page="+artistSearchQuery.perPage(), urlNext );
        assertEquals(currentURI+"?q="+artistSearchQuery.query()+"&page="+(artistSearchQuery.page()-1)+"&per_page="+artistSearchQuery.perPage(), urlPrev );
        assertEquals(currentURI+"?q="+artistSearchQuery.query()+"&page=1&per_page="+artistSearchQuery.perPage(), urlFirst );
        assertEquals(currentURI+"?q="+artistSearchQuery.query()+"&page="+result.get("pagination").get("pages").asInt()+"&per_page="+artistSearchQuery.perPage(), urlLast );


        // Verify that the repository method is called with the correct arguments
        then(discogsAPIClient).should()
                             .searchArtist(eq(artistSearchQuery));


    }

    @Test
    void searchArtist_WhenArtistNotExists() throws JsonProcessingException {
        // Given - Build the ArtistSearchQuery and mock the DiscogsAPIClient response

        String currentURI= "http://domain.com/v1/discogs/artists/search";
        Integer page = 2;
        Integer perPage = 50;
        ArtistSearchQuery artistSearchQuery = new ArtistSearchQuery("Nirvana", currentURI, page, perPage);

        JsonNode jsonNode = getJsonNode("notExists");
        when(discogsAPIClient.searchArtist(any())).thenReturn(jsonNode);

        // When - Call the searchArtist method
        JsonNode result = searchArtistFromDiscogsService.searchArtist(artistSearchQuery);
        // Then - Verify that the result is correct
        // Assert that the result is present
        assertNotNull(result);
        JsonNode results = result.get("results");
        // validate results is not null
        assertNotNull(results);
        // validate results is  empty
        assertTrue(results.isArray() && results.isEmpty());


        // Verify that the repository method is called with the correct arguments
        then(discogsAPIClient).should()
                              .searchArtist(eq(artistSearchQuery));


    }

    @Test
    void searchArtist_ThrowDiscogsException()  {
        // Given - Build the ArtistSearchQuery and mock the DiscogsAPIClient response

        String currentURI= "http://domain.com/v1/discogs/artists/search";
        Integer page = 2;
        Integer perPage = 50;
        ArtistSearchQuery artistSearchQuery = new ArtistSearchQuery("Nirvana", currentURI, page, perPage);


        when(discogsAPIClient.searchArtist(any())).thenThrow(new DiscogsException("Discogs API returns error"));

        // When - Call the searchArtist method
        try {
            discogsAPIClient.searchArtist(artistSearchQuery);
        } catch (RuntimeException e) {
            // Verify the exception was thrown
            assertInstanceOf(DiscogsException.class, e);
        }

    }





    private static JsonNode getJsonNode(String type) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = switch (type) {
            case "exists" -> """
                    {"pagination":{"page":2,"pages":9,"per_page":50,"items":404,"urls":{"first":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=1&per_page=50","last":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=9&per_page=50","prev":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=1&per_page=50","next":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=3&per_page=50"}},"results":[{"id":125246,"type":"artist","master_id":null,"master_url":null,"uri":"/artist/125246-Nirvana","title":"Nirvana","thumb":"https://i.discogs.com/KydDnAWdAzeHy0dZ4YSnpkuh__uLXIk8w60uKQVW0G4/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","cover_image":"https://i.discogs.com/S_HB3FZR5TTcRyjBqlnUQpF_WgF-i9iSqcMTQGdwB6M/rs:fit/g:sm/q:90/h:609/w:600/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","resource_url":"https://api.discogs.com/artists/125246"}]}
                    """;
            case "notExists" -> """
                    {"pagination":{"page":1,"pages":1,"per_page":50,"items":0,"urls":{}},"results":[]}
                    """;
            default -> "";
        };
        return objectMapper.readTree(jsonString);
    }
}
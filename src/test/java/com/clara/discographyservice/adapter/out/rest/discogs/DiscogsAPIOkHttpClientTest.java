package com.clara.discographyservice.adapter.out.rest.discogs;

import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.clara.discographyservice.application.port.out.DiscogsException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiscogsAPIOkHttpClientTest {

    private static MockWebServer mockWebServer;
    private DiscogsAPIOkHttpClient discogsAPIOkHttpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String consumerKey = "testKey";
    private final String consumerSecret = "testSecret";


    static void shutdownMockWebServer() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void setUp() {
        mockWebServer = new MockWebServer();
        try {
            mockWebServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String baseUrl = mockWebServer.url("/").toString();
        discogsAPIOkHttpClient = new DiscogsAPIOkHttpClient(
                new OkHttpClient(),
                objectMapper,
                consumerKey,
                consumerSecret,
                baseUrl
        );
    }

    @Test
    void testSearchArtist_SuccessfulResponse()  {
        setUp();
        // Given
        // Mock successful response
        String jsonResponse = """
        {"pagination":{"page":2,"pages":9,"per_page":50,"items":404,"urls":{"first":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=1&per_page=50","last":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=9&per_page=50","prev":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=1&per_page=50","next":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=3&per_page=50"}},"results":[{"id":125246,"type":"artist","master_id":null,"master_url":null,"uri":"/artist/125246-Nirvana","title":"Nirvana","thumb":"https://i.discogs.com/KydDnAWdAzeHy0dZ4YSnpkuh__uLXIk8w60uKQVW0G4/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","cover_image":"https://i.discogs.com/S_HB3FZR5TTcRyjBqlnUQpF_WgF-i9iSqcMTQGdwB6M/rs:fit/g:sm/q:90/h:609/w:600/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","resource_url":"https://api.discogs.com/artists/125246"}]}
        """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        ArtistSearchQuery query = new ArtistSearchQuery("Nirvana", "http://domain.com/v1/discogs/artists/search" , 1, 50);
        // when
        JsonNode result = discogsAPIOkHttpClient.searchArtist(query);

        // then - Verify that the result is correct
        assertNotNull(result);
        assertEquals(2, result.get("pagination").get("page").asInt());
        assertEquals(50, result.get("pagination").get("per_page").asInt());
        assertEquals(404, result.get("pagination").get("items").asInt());
        assertEquals("Nirvana", result.get("results").get(0).get("title").asText());
        shutdownMockWebServer();
    }

    @Test
    void testSearchArtist_UnsuccessfulResponse() {
        setUp();
        // Given
        // Mock 500 response
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        ArtistSearchQuery query = new ArtistSearchQuery("Nirvana", "http://domain.com/v1/discogs/artists/search" , 1, 50);
        //when
        DiscogsException exception = assertThrows(
                DiscogsException.class,
                () -> discogsAPIOkHttpClient.searchArtist(query)
        );

        // then
        assertTrue(exception.getMessage().contains("Discogs API error: HTTP"));
        shutdownMockWebServer();
    }


    @Test
    void testSearchArtist_IOException() {
        setUp();
        // Given
        // Simulate IOException by shutting down the server
        shutdownMockWebServer();

        ArtistSearchQuery query = new ArtistSearchQuery("Nirvana", "http://domain.com/v1/discogs/artists/search" , 1, 50);
        // when
        DiscogsException exception = assertThrows(
                DiscogsException.class,
                () -> discogsAPIOkHttpClient.searchArtist(query)
        );
        // Then
        assertTrue(exception.getMessage().contains("Discogs API error, unexpected ex"));
        shutdownMockWebServer();
    }

    @Test
    void testConstructor_NullBaseUrl() {
        setUp();
        // Validate the constructor throws an exception when the base URL is null
        OkHttpClient client = new OkHttpClient();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DiscogsAPIOkHttpClient(
                        client,
                        objectMapper,
                        consumerKey,
                        consumerSecret,
                        null
                )
        );


        assertEquals("Base URL cannot be null", exception.getMessage());
        shutdownMockWebServer();
    }

    @Test
    void testConstructor_InvalidBaseUrl() {
        // Validate the constructor throws an exception when the base URL is null
        OkHttpClient client = new OkHttpClient();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> {
                    new DiscogsAPIOkHttpClient(
                            client,
                            objectMapper,
                            consumerKey,
                            consumerSecret,
                            "invalid url"
                    );
                });

        assertEquals("Base URL is invalid", exception.getMessage());
    }

    @Test
    void testConstructor_BlankConsumerKey() {
        setUp();
        // Validate the constructor throws an exception when the consumer key is blank
        OkHttpClient client = new OkHttpClient();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DiscogsAPIOkHttpClient(
                        client,
                        objectMapper,
                        "",
                        consumerSecret,
                        "https://api.discogs.com"
                )
        );

        assertEquals("Consumer key cannot be null", exception.getMessage());
        shutdownMockWebServer();
    }

    @Test
    void testConstructor_BlankConsumerSecret() {
        setUp();
        // Validate the constructor throws an exception when the consumer secret is blank
        OkHttpClient client = new OkHttpClient();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> {
                    new DiscogsAPIOkHttpClient(
                            client,
                            objectMapper,
                            consumerKey,
                            null,
                            "https://api.discogs.com"
                    );
                });

        assertEquals("Consumer secret cannot be null", exception.getMessage());
        shutdownMockWebServer();
    }
    @Test
    void testSearchArtist_InvalidBaseUrlInSearch() {

        DiscogsAPIOkHttpClient client = new DiscogsAPIOkHttpClient(
                new OkHttpClient(), objectMapper, consumerKey, consumerSecret, "http://domain.com/v1/discogs/artists/search"
        );

        assertThrows(IllegalArgumentException.class, () -> client.searchArtist(null));
    }
}

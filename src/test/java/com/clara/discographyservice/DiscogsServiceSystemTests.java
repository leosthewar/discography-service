package com.clara.discographyservice;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponentsBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class DiscogsServiceSystemTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String DISCOGS_RESOURCE_ENDPOINT = "/v1/discogs/artists";

    private WireMockServer wireMockServer;
    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(8080));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
    }

    @AfterEach
    void teardown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }


    @Test
    void searchArtist_returnsResults()  {

        //Given - Build the URL with query parameters
        String baseUrl = DISCOGS_RESOURCE_ENDPOINT+"/search";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .queryParam("q", "Nirvana")
                                         .toUriString();

        // mock the response from the Discogs API
        stubFor(get(urlPathEqualTo("/database/search"))
                .withQueryParam("q", equalTo("Nirvana"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {"pagination":{"page":1,"pages":9,"per_page":50,"items":10,"urls":{"last":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=9&per_page=50","next":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=2&per_page=50"}},"results":[{"id":125246,"type":"artist","master_id":null,"master_url":null,"uri":"/artist/125246-Nirvana","title":"Nirvana","thumb":"https://i.discogs.com/KydDnAWdAzeHy0dZ4YSnpkuh__uLXIk8w60uKQVW0G4/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","cover_image":"https://i.discogs.com/S_HB3FZR5TTcRyjBqlnUQpF_WgF-i9iSqcMTQGdwB6M/rs:fit/g:sm/q:90/h:609/w:600/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","resource_url":"https://api.discogs.com/artists/125246"}]}
                        """)
                        .withStatus(200)));

        //When - Make the request
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(uri, JsonNode.class);

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        then(response.getBody().get("results")).isNotNull();
        assertEquals(10,(response.getBody().get("pagination").get("items").asInt()));
        assertEquals("Nirvana",(response.getBody().get("results").get(0).get("title").asText()));

        }

    @Test
    void searchArtist_returnsResultsWithPagination()  {

        //Given - Build the URL with query parameters
        String baseUrl = DISCOGS_RESOURCE_ENDPOINT+"/search";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .queryParam("q", "Nirvana")
                                         .queryParam("page", "2")
                                         .queryParam("per_page", "30")
                                         .toUriString();

        // mock the response from the Discogs API
        stubFor(get(urlPathEqualTo("/database/search"))
                .withQueryParam("q", equalTo("Nirvana"))
                .withQueryParam("page", equalTo("2"))
                .withQueryParam("per_page", equalTo("30"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {"pagination":{"page":2,"pages":9,"per_page":30,"items":10,"urls":{"last":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=9&per_page=50","next":"https://api.discogs.com/database/search?q=Nirvana&type=artist&page=2&per_page=50"}},"results":[{"id":125246,"type":"artist","master_id":null,"master_url":null,"uri":"/artist/125246-Nirvana","title":"Nirvana","thumb":"https://i.discogs.com/KydDnAWdAzeHy0dZ4YSnpkuh__uLXIk8w60uKQVW0G4/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","cover_image":"https://i.discogs.com/S_HB3FZR5TTcRyjBqlnUQpF_WgF-i9iSqcMTQGdwB6M/rs:fit/g:sm/q:90/h:609/w:600/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","resource_url":"https://api.discogs.com/artists/125246"}]}
                        """)
                        .withStatus(200)));

        //When - Make the request
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(uri, JsonNode.class);

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        then(response.getBody().get("results")).isNotNull();
        assertEquals(10,(response.getBody().get("pagination").get("items").asInt()));
        assertEquals("Nirvana",(response.getBody().get("results").get(0).get("title").asText()));

    }

    @Test
    void searchArtist_returnsEmptyResults()  {

        //Given - Build the URL with query parameters
        String baseUrl = DISCOGS_RESOURCE_ENDPOINT+"/search";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .queryParam("q", "Nirvanaqqqq")
                                         .toUriString();

        // mock the response from the Discogs API
        stubFor(get(urlPathEqualTo("/database/search"))
                .withQueryParam("q", equalTo("Nirvanaqqqq"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {"pagination":{"page":1,"pages":1,"per_page":50,"items":0,"urls":{}},"results":[]}
                        """)
                        .withStatus(200)));

        //When - Make the request
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(uri, JsonNode.class);

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        then(response.getBody().get("results")).isNotNull();
        then(response.getBody().get("results")).isEmpty();

    }

    @Test
    void searchArtist_DiscogsAPIException()  {

        //Given - Build the URL with query parameters
        String baseUrl = DISCOGS_RESOURCE_ENDPOINT+"/search";
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                                         .queryParam("q", "Nirvana")
                                         .toUriString();

        // mock the response from the Discogs API
        stubFor(get(urlPathEqualTo("/database/search"))
                .withQueryParam("q", equalTo("Nirvana"))
                .willReturn(aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        //When - Make the request
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(uri, JsonNode.class);

        //Then - Check the response
        then(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_GATEWAY);
        then(response.getBody().get("code").asInt()).isEqualTo(502);

    }

}

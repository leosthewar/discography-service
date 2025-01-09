package com.clara.discographyservice.adapter.in.rest;

import com.clara.discographyservice.application.port.in.SearchArtistFromDiscogsUseCase;
import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.clara.discographyservice.application.port.out.DiscogsException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the DiscogsRestController.
 * <p>
 * This class includes test cases to verify the behavior of the DiscogsRestController endpoints,
 * ensuring that the correct HTTP status codes and response messages are returned under various scenarios.
 */
@WebMvcTest(controllers = DiscogsRestController.class)
class DiscogsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchArtistFromDiscogsUseCase searchArtistDiscogsUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ENDPOINT = "/v1/discogs/artists";

    @Test
    void testSearchArtist_Success() throws Exception {
        // Given
        String query = "Nirvana";
        int page = 1;
        int perPage = 10;
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/search")
                                      .queryParam("q", query)
                                      .queryParam("page", page)
                                      .queryParam("per_page", perPage)
                                      .build()
                                      .toUri();

        JsonNode mockResponse = objectMapper.readTree("""
                {"pagination":{"page":2,"pages":9,"per_page":50,"items":405,"urls":{"first":"http://localhost:8080/v1/discogs/artists/search?q=Nirvana&page=1&per_page=50","last":"http://localhost:8080/v1/discogs/artists/search?q=Nirvana&page=9&per_page=50","prev":"http://localhost:8080/v1/discogs/artists/search?q=Nirvana&page=1&per_page=50","next":"http://localhost:8080/v1/discogs/artists/search?q=Nirvana&page=3&per_page=50"}},"results":[{"id":3221382,"type":"artist","master_id":null,"master_url":null,"uri":"/artist/3221382-Nirvana-Groove","title":"Nirvana Groove","thumb":"https://i.discogs.com/M9YUJvwbMf3oMKV3cSjBsb_DNcnig3LnraDwsH9bGmY/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTMyMjEz/ODItMTU5NjE3ODM5/MS03ODY0LmpwZWc.jpeg","cover_image":"https://i.discogs.com/Fygscqm4p_3qrRn8VcZENKkCSf1Md9LUDi-r7p34OLk/rs:fit/g:sm/q:90/h:769/w:600/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTMyMjEz/ODItMTU5NjE3ODM5/MS03ODY0LmpwZWc.jpeg","resource_url":"https://api.discogs.com/artists/3221382"}]}
                """
        );

        when(searchArtistDiscogsUseCase.searchArtist(any(ArtistSearchQuery.class)))
                .thenReturn(mockResponse);

        // When
        ResultActions results = mockMvc.perform(
                get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then - verify the response
        results.andExpect(status().isOk())
               .andExpect(jsonPath("$.pagination.page").value(2))
               .andExpect(jsonPath("$.results[0].title").value("Nirvana Groove"));
    }

    @Test
    void testSearchArtist_BadRequestEmptyQuery() throws Exception {
        // Given
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/search")
                                      .build()
                                      .toUri();

        // When
        ResultActions results = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)
        );

        // Then - verify the response
        results.andExpect(status().isBadRequest());
    }

    @Test
    void testSearchArtist_BadRequestWithNegativeParameters() throws Exception {
        // Given
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/search")
                                      .queryParam("q", "Nirvana")
                                      .queryParam("page", -5)
                                      .queryParam("per_page", -5)
                                      .build()
                                      .toUri();

        // When
        ResultActions results = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON)
        );

        // Then - verify the response
        results.andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void testSearchArtist_InternalServerError() throws Exception {
        // Given
        String query = "Nirvana";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/search")
                                      .queryParam("q", query)
                                      .build()
                                      .toUri();

        when(searchArtistDiscogsUseCase.searchArtist(any(ArtistSearchQuery.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResultActions results = mockMvc.perform(
                get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then - verify the response
        results.andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testSearchArtist_DiscogsError() throws Exception {
        // Given
        String query = "Nirvana";
        URI uri = UriComponentsBuilder.fromUriString(ENDPOINT + "/search")
                                      .queryParam("q", query)
                                      .build()
                                      .toUri();

        when(searchArtistDiscogsUseCase.searchArtist(any(ArtistSearchQuery.class)))
                .thenThrow(new DiscogsException("Discogs API error"));

        // When
        ResultActions results = mockMvc.perform(
                get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then - verify the response
        results.andExpect(status().isBadGateway())
               .andExpect(jsonPath("$.code").value(502));
    }
}

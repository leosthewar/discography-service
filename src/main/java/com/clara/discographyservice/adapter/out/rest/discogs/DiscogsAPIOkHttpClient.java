package com.clara.discographyservice.adapter.out.rest.discogs;

import com.clara.discographyservice.application.port.in.ArtistGetQuery;
import com.clara.discographyservice.application.port.in.ArtistSearchQuery;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.clara.discographyservice.application.port.out.DiscogsException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class DiscogsAPIOkHttpClient implements DiscogsAPIClient {


    private static final Logger logger = LoggerFactory.getLogger(DiscogsAPIOkHttpClient.class);
    private static final  String SEARCH_RESOURCE = "/database/search";
    private static final  String ARTISTS_RESOURCE = "/artists";


    private final String baseUrl;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final String consumerKey;
    private final String consumerSecret;


    public DiscogsAPIOkHttpClient(OkHttpClient client, ObjectMapper objectMapper,
                                  String consumerKey, String consumerSecret,
                                  String baseUrl) {
        this.client = Objects.requireNonNull(client);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.baseUrl = baseUrl;
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Base URL cannot be null");
        }
        if (HttpUrl.parse(baseUrl) == null) {
            throw new IllegalArgumentException("Base URL is invalid");
        }
        if (consumerKey == null || consumerKey.isBlank()) {
            throw new IllegalArgumentException("Consumer key cannot be null");
        }

        if (consumerSecret == null || consumerSecret.isBlank()) {
            throw new IllegalArgumentException("Consumer secret cannot be null");
        }
    }

    /**
     * A client for interacting with the Discogs API using OkHttpClient.
     * Provides functionality to search for artists using the Discogs API.
     * returns a JsonNode containing the search results from Discogs API
     *
     * @throws DiscogsException         when Discogs API returns an error or occurs an unexpected error processing the response
     * @throws IllegalArgumentException when  occurs  an error creating the HttpUrl or Request
     */
    @Nonnull
    @Override
    public JsonNode searchArtist(ArtistSearchQuery artistQuery) {
        Request request;
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + SEARCH_RESOURCE)
                                 .newBuilder()
                                 .addQueryParameter("q", artistQuery.query())
                                 .addQueryParameter("type", "artist")
                                 .addQueryParameter("page", artistQuery.page()
                                                                       .toString())
                                 .addQueryParameter("per_page", artistQuery.perPage()
                                                                           .toString())
                                 .build();

            request = buildRequest(url,true);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error creating Discogs request: " + e.getMessage(), e);
        }

        try (Response response = client.newCall(request)
                                       .execute()) {

            if (!response.isSuccessful()) {
                throw new DiscogsException(String.format("Discogs API error: HTTP %d, message: %s", response.code(), response.message()));
            }
            return objectMapper.readTree(response.body()
                                                 .string());
        } catch (IOException e) {
            throw new DiscogsException("Discogs API error, unexpected ex: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<JsonNode> getArtist(ArtistGetQuery artistGetQuery) {

        Request request;
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + ARTISTS_RESOURCE)
                                 .newBuilder()
                                 .addEncodedPathSegment(artistGetQuery.artistId()
                                                                      .toString())
                                 .build();

            request = buildRequest(url,true);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected error creating Discogs request: " + e.getMessage(), e);
        }

        try (Response response = client.newCall(request)
                                       .execute()) {

            if (response.code() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            }

            if(response.isSuccessful()){

                String responseBody = response.body().string();
                logger.info("Response: {}", responseBody);
                return Optional.of(objectMapper.readTree(responseBody));
            }else{
                throw new DiscogsException(String.format("Discogs API error: HTTP %d, message: %s", response.code(), response.message()));
            }

        } catch (IOException e) {
            throw new DiscogsException("Discogs API error, unexpected ex: " + e.getMessage(), e);
        }
    }

    private Request buildRequest(HttpUrl url, boolean requiresAuth) {
        Request.Builder rBuilder = new  Request.Builder();
        if (requiresAuth) {
            rBuilder.addHeader("Authorization", String.format("Discogs key=%s, secret=%s", consumerKey, consumerSecret));
        }
        return rBuilder
                .addHeader("User-Agent", "clara-discography-service")
                .url(url)
                .get()
                .build();

    }


}

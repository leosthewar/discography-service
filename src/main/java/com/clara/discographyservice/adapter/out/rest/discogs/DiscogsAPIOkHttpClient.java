package com.clara.discographyservice.adapter.out.rest.discogs;

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

import java.io.IOException;
import java.util.Objects;


public class DiscogsAPIOkHttpClient implements DiscogsAPIClient {


    private static final Logger logger = LoggerFactory.getLogger(DiscogsAPIOkHttpClient.class);

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
            HttpUrl url = HttpUrl.parse(baseUrl + "/database/search")
                                 .newBuilder()
                                 .addQueryParameter("q", artistQuery.query())
                                 .addQueryParameter("type", "artist")
                                 .addQueryParameter("page", artistQuery.page().toString())
                                 .addQueryParameter("per_page", artistQuery.perPage().toString())
                                 .build();

            request = buildRequest(url);
        } catch (Exception e) {
            logger.error("Unexpected error creating Discogs request: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Unexpected error creating Discogs request: " + e.getMessage(), e);
        }

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new DiscogsException(String.format("Discogs API error: HTTP %d, message: %s", response.code(), response.message()));
            }
            return objectMapper.readTree(response.body().string());
        } catch (IOException e) {
            logger.error("Discogs API error, unexpected ex: {}", e.getMessage(), e);
            throw new DiscogsException("Discogs API error, unexpected ex: " + e.getMessage(), e);
        }
    }
    private Request buildRequest(HttpUrl url) {
        return new Request.Builder()
                .addHeader("Authorization", String.format("Discogs key=%s, secret=%s", consumerKey, consumerSecret))
                .url(url)
                .get()
                .build();
    }


}

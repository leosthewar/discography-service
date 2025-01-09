package com.clara.discographyservice.adapter.out.rest.discogs;

import com.clara.discographyservice.application.port.in.model.ArtistGetQuery;
import com.clara.discographyservice.application.port.in.model.ArtistReleasesGetQuery;
import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.clara.discographyservice.application.port.in.model.ReleaseGetQuery;
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

    // API resources
    private static final String SEARCH_RESOURCE = "/database/search";
    private static final String ARTISTS_RESOURCE = "/artists";
    private static final String ARTISTS_RELEASES_RESOURCE = "/artists/%s/releases";
    private static final String RELEASE_RESOURCE = "/releases";

    // Common messages
    private static final String ERROR_CREATING_REQUEST = "Unexpected error creating Discogs request: %s";
    private static final String API_ERROR_MESSAGE = "Discogs API error: HTTP %d, message: %s";
    private static final String API_ERROR_UNEXPECTED = "Discogs API error, unexpected ex: %s";
    private static final String BASE_URL_NULL_ERROR = "Base URL cannot be null";
    private static final String BASE_URL_INVALID_ERROR = "Base URL is invalid";
    private static final String CONSUMER_KEY_ERROR = "Consumer key cannot be null";
    private static final String CONSUMER_SECRET_ERROR = "Consumer secret cannot be null";

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
            throw new IllegalArgumentException(BASE_URL_NULL_ERROR);
        }
        if (HttpUrl.parse(baseUrl) == null) {
            throw new IllegalArgumentException(BASE_URL_INVALID_ERROR);
        }
        if (consumerKey == null || consumerKey.isBlank()) {
            throw new IllegalArgumentException(CONSUMER_KEY_ERROR);
        }
        if (consumerSecret == null || consumerSecret.isBlank()) {
            throw new IllegalArgumentException(CONSUMER_SECRET_ERROR);
        }
    }

    @Nonnull
    @Override
    public JsonNode searchArtist(ArtistSearchQuery artistQuery) {
        Request request;
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + SEARCH_RESOURCE)
                                 .newBuilder()
                                 .addQueryParameter("q", artistQuery.query())
                                 .addQueryParameter("type", "artist")
                                 .addQueryParameter("page", artistQuery.page().toString())
                                 .addQueryParameter("per_page", artistQuery.perPage().toString())
                                 .build();

            request = buildRequest(url);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(ERROR_CREATING_REQUEST, e.getMessage()), e);
        }

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new DiscogsException(String.format(API_ERROR_MESSAGE, response.code(), response.message()));
            }
            return objectMapper.readTree(response.body().string());
        } catch (IOException e) {
            throw new DiscogsException(String.format(API_ERROR_UNEXPECTED, e.getMessage()), e);
        }
    }

    @Override
    public Optional<JsonNode> getArtist(ArtistGetQuery artistGetQuery) {

        Request request;
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + ARTISTS_RESOURCE)
                                 .newBuilder()
                                 .addPathSegment(artistGetQuery.discogsArtistId().toString())
                                 .build();

            request = buildRequest(url);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(ERROR_CREATING_REQUEST, e.getMessage()), e);
        }

        try (Response response = client.newCall(request).execute()) {

            if (response.code() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            }

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                logger.debug("getArtist Response: {}", responseBody);
                return Optional.of(objectMapper.readTree(responseBody));
            } else {
                throw new DiscogsException(String.format(API_ERROR_MESSAGE, response.code(), response.message()));
            }

        } catch (IOException e) {
            throw new DiscogsException(String.format(API_ERROR_UNEXPECTED, e.getMessage()), e);
        }
    }

    @Override
    public Optional<JsonNode> getArtistReleases(ArtistReleasesGetQuery artistReleasesGetQuery) {
        Request request;
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + String.format(ARTISTS_RELEASES_RESOURCE, artistReleasesGetQuery.artistId()))
                                 .newBuilder()
                                 .addQueryParameter("page", artistReleasesGetQuery.page().toString())
                                 .addQueryParameter("per_page", artistReleasesGetQuery.perPage().toString())
                                 .build();

            request = buildRequest(url);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(ERROR_CREATING_REQUEST, e.getMessage()), e);
        }

        try (Response response = client.newCall(request).execute()) {

            if (response.code() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            }

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                logger.debug("getArtistReleases Response: {}", responseBody);
                return Optional.of(objectMapper.readTree(responseBody));
            } else {
                throw new DiscogsException(String.format(API_ERROR_MESSAGE, response.code(), response.message()));
            }

        } catch (IOException e) {
            throw new DiscogsException(String.format(API_ERROR_UNEXPECTED, e.getMessage()), e);
        }
    }

    @Override
    public Optional<JsonNode> getRelease(ReleaseGetQuery releaseGetQuery) {
        Request request;
        try {
            HttpUrl url = HttpUrl.parse(baseUrl + RELEASE_RESOURCE)
                                 .newBuilder()
                                 .addPathSegment(releaseGetQuery.releaseId().toString())
                                 .build();

            request = buildRequest(url);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(ERROR_CREATING_REQUEST, e.getMessage()), e);
        }

        try (Response response = client.newCall(request).execute()) {

            if (response.code() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            }

            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                logger.debug("getRelease Response: {}", responseBody);
                return Optional.of(objectMapper.readTree(responseBody));
            } else {
                throw new DiscogsException(String.format(API_ERROR_MESSAGE, response.code(), response.message()));
            }

        } catch (IOException e) {
            throw new DiscogsException(String.format(API_ERROR_UNEXPECTED, e.getMessage()), e);
        }
    }

    private Request buildRequest(HttpUrl url) {
        return new Request.Builder()
                .addHeader("Authorization", String.format("Discogs key=%s, secret=%s", consumerKey, consumerSecret))
                .addHeader("User-Agent", "clara-discography-service")
                .url(url)
                .get()
                .build();
    }
}

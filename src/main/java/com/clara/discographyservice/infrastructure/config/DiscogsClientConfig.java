package com.clara.discographyservice.infrastructure.config;

import com.clara.discographyservice.adapter.out.rest.discogs.DiscogsAPIOkHttpClient;
import com.clara.discographyservice.application.port.out.DiscogsAPIClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscogsClientConfig {

    @Value("${discogs.consumerKey}")
    private String consumerKey;

    @Value("${discogs.consumerSecret}")
    private String consumerSecret;

    @Value("${discogs.baseUrl}")
    private String baseUrl;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Bean
    public DiscogsAPIClient discogsApiClient(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        return new DiscogsAPIOkHttpClient(okHttpClient, objectMapper, consumerKey, consumerSecret,baseUrl);
    }
}

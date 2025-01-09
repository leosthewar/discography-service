package com.clara.discographyservice.application.port.in.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import static com.clara.discographyservice.infrastructure.validation.BeanValidation.validate;


public record ArtistSearchQuery(
        @NotBlank String query,
        @NotBlank String currentURI,
        @Positive Integer page,
        @Positive Integer perPage) {

    public ArtistSearchQuery( String query,String currentURI, Integer page, Integer perPage ) {
        this.query = query;
        this.currentURI = currentURI;
        this.page = page == null ? 1 : page;
        this.perPage = perPage == null ? 50 : perPage;
        validate(this);
    }
}

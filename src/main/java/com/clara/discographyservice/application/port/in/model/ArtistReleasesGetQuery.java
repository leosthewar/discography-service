package com.clara.discographyservice.application.port.in.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.clara.discographyservice.infrastructure.validation.BeanValidation.validate;


public record ArtistReleasesGetQuery(
        @NotNull @Positive Long artistId, Integer page, Integer perPage) {

    public ArtistReleasesGetQuery(Long artistId, Integer page, Integer perPage) {
        this.artistId = artistId;
        this.page = page == null ? 1 : page;
        this.perPage = perPage == null ? 100 : perPage;
        validate(this);
    }

    public ArtistReleasesGetQuery(Long artistId, Integer page) {
       this(artistId, page, null);
        validate(this);
    }
    public ArtistReleasesGetQuery(Long artistId) {
        this(artistId, null,null);
        validate(this);
    }
}

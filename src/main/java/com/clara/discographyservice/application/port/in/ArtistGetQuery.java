package com.clara.discographyservice.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.clara.discographyservice.infrastructure.validation.BeanValidation.validate;


public record ArtistGetQuery(
        @NotNull @Positive Long artistId) {

    public ArtistGetQuery(Long artistId) {
        this.artistId = artistId;
        validate(this);
    }
}

package com.clara.discographyservice.application.port.in.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.clara.discographyservice.infrastructure.validation.BeanValidation.validate;

public record ArtistDiscographyImportCommand(
      @NotNull @Positive Long discogsArtistId) {

    public ArtistDiscographyImportCommand(Long discogsArtistId ) {
        this.discogsArtistId = discogsArtistId;
        validate(this);
    }
}

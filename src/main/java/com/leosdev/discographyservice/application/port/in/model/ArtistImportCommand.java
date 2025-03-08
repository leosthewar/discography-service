package com.leosdev.discographyservice.application.port.in.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.leosdev.discographyservice.infrastructure.validation.BeanValidation.validate;

public record ArtistImportCommand(
      @NotNull @Positive Long discogsArtistId) {

    public ArtistImportCommand(Long discogsArtistId ) {
        this.discogsArtistId = discogsArtistId;
        validate(this);
    }
}

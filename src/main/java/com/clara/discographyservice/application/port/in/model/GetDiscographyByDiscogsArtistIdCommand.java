package com.clara.discographyservice.application.port.in.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import static com.clara.discographyservice.infrastructure.validation.BeanValidation.validate;


public record GetDiscographyByDiscogsArtistIdCommand(
        @NotNull @Positive Long discogsArtistId,
        @PositiveOrZero Integer page,
        @Positive @Max(100) Integer size,
        String sort,
        String sortDirection) {

    public GetDiscographyByDiscogsArtistIdCommand(Long discogsArtistId,
                                                  Integer page,
                                                  Integer size,
                                                  String sortDirection) {
        this(
                discogsArtistId,
                page == null ? 0 : page,
                size == null ?  50 : size,
                "r.year",
                sortDirection == null ? "asc" : sortDirection
        );
        validate(this);
    }
}

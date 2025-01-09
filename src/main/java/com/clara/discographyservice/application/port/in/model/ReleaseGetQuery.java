package com.clara.discographyservice.application.port.in.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.clara.discographyservice.infrastructure.validation.BeanValidation.validate;


public record ReleaseGetQuery(
        @NotNull @Positive Long releaseId) {

    public ReleaseGetQuery(Long releaseId) {
        this.releaseId = releaseId;
        validate(this);
    }

}

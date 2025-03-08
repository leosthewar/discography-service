package com.leosdev.discographyservice.application.port.in.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.leosdev.discographyservice.infrastructure.validation.BeanValidation.validate;


public record ReleaseGetQuery(
        @NotNull @Positive Long releaseId) {

    public ReleaseGetQuery(Long releaseId) {
        this.releaseId = releaseId;
        validate(this);
    }

}

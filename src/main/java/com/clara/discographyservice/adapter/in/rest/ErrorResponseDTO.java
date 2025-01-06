package com.clara.discographyservice.adapter.in.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(name = "error", description = "A error response")
public record ErrorResponseDTO(Integer code, String message, String details, LocalDateTime timestamp) {
}

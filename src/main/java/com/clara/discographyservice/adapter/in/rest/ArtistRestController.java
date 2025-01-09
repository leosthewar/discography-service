package com.clara.discographyservice.adapter.in.rest;

import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistResModel;
import com.clara.discographyservice.application.port.in.ImportArtistUseCase;
import com.clara.discographyservice.application.port.in.model.ArtistImportCommand;
import com.clara.discographyservice.application.port.in.model.ImportArtistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("v1/artists")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "502", description = "Error in the Discogs API", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
})
class ArtistRestController {

    private final ImportArtistUseCase importArtistUseCase;

    public ArtistRestController(ImportArtistUseCase importArtistUseCase) {
        this.importArtistUseCase = importArtistUseCase;
    }


    @Operation(summary = "Import artist's data from  Discogs API")
    @PostMapping("/import/discogs/{discogsArtistId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist already imported", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
            @ApiResponse(responseCode = "201", description = "Artist imported successfully from Discogs", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
    })
    public ResponseEntity<Object> importArtist(@PathVariable(value = "discogsArtistId") Long discogsArtistId) {
        Optional<ImportArtistResponse> response = importArtistUseCase.importArtist(new ArtistImportCommand(discogsArtistId));

        if (response.isPresent()) {
            return response.get().imported() ?
                    ResponseEntity.status(201).body(response.get().artist()) :
                    ResponseEntity.ok(response.get().artist());
        }

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(404, "Artist not found", "Artist not found in Discogs", java.time.LocalDateTime.now());
        return ResponseEntity.status(404).body(errorResponse);
    }

}

package com.clara.discographyservice.adapter.in.rest;

import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistResModel;
import com.clara.discographyservice.application.port.in.ArtistDiscographyImportCommand;
import com.clara.discographyservice.application.port.in.ImportArtistDiscographyResponse;
import com.clara.discographyservice.application.port.in.ImportArtistDiscographyUseCase;
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


@RestController
@RequestMapping("v1/artists/discography")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "502", description = "Discogs API returns error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
})
class ArtistDiscographyRestController {

    private final ImportArtistDiscographyUseCase importArtistDiscographyUseCase;

    public ArtistDiscographyRestController(ImportArtistDiscographyUseCase importArtistDiscographyUseCase) {
        this.importArtistDiscographyUseCase = importArtistDiscographyUseCase;
    }


    @Operation(summary = "Import artist's data from  Discogs API")
    @PostMapping("/import/discogs/{discogsArtistId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist's discography already created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
            @ApiResponse(responseCode = "201", description = "Artist's discography created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
            @ApiResponse(responseCode = "422", description = "Error creating importation request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
    })
    public ResponseEntity<Object> searchArtist(@PathVariable(value = "discogsArtistId") Long discogsArtistId) {
        ImportArtistDiscographyResponse response = importArtistDiscographyUseCase.createArtistDiscographyImport(new ArtistDiscographyImportCommand(discogsArtistId));

        if (response.error()) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(422, "Error creating importation request", response.message(), java.time.LocalDateTime.now());
            return ResponseEntity.status(422)
                                 .body(errorResponse);
        }
        ArtistDicographyImportResponseDTO responseDTO = new ArtistDicographyImportResponseDTO(response.message(),
                response.artistDiscographyImport().getId(),
                response.artistDiscographyImport().getDiscogsArtistId(),
                response.artistDiscographyImport().getStatus().name());

        return ResponseEntity.status(response.created() ? 201 : 200).body(responseDTO);
    }

}

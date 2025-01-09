package com.clara.discographyservice.adapter.in.rest;

import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistResModel;
import com.clara.discographyservice.application.port.in.ArtistDiscographyUseCase;
import com.clara.discographyservice.application.port.in.ImportArtistDiscographyUseCase;
import com.clara.discographyservice.application.port.in.model.ArtistDiscographyImportCommand;
import com.clara.discographyservice.application.port.in.model.GetArtistDiscographyResponse;
import com.clara.discographyservice.application.port.in.model.GetDiscographyByDiscogsArtistIdCommand;
import com.clara.discographyservice.application.port.in.model.ImportArtistDiscographyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/artists/discography")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "502", description = "Error in the Discogs API", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Artist not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
})
class ArtistDiscographyRestController {

    private final ImportArtistDiscographyUseCase importArtistDiscographyImportUseCase;
    private final ArtistDiscographyUseCase artistDiscographyUseCase;


    public ArtistDiscographyRestController(ImportArtistDiscographyUseCase importArtistDiscographyImportUseCase,
                                           ArtistDiscographyUseCase artistDiscographyService ) {
        this.importArtistDiscographyImportUseCase = importArtistDiscographyImportUseCase;
        this.artistDiscographyUseCase =artistDiscographyService;
    }


    @Operation(summary = "Import an artist from  Discogs API")
    @PostMapping("/import/discogs/{discogsArtistId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist's discography already created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
            @ApiResponse(responseCode = "201", description = "Artist's discography created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
            @ApiResponse(responseCode = "422", description = "Error creating importation request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistResModel.class))),
    })
    public ResponseEntity<Object> searchArtist(@PathVariable(value = "discogsArtistId") Long discogsArtistId) {
        ImportArtistDiscographyResponse response = importArtistDiscographyImportUseCase.createArtistDiscographyImport(new ArtistDiscographyImportCommand(discogsArtistId));

        if (response.error()) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(422, "Error creating importation request", response.message(), java.time.LocalDateTime.now());
            return ResponseEntity.status(422)
                                 .body(errorResponse);
        }
        ArtistDicographyImportResponseDTO responseDTO = new ArtistDicographyImportResponseDTO(response.message(),
                response.artistDiscographyImport().id(),
                response.artistDiscographyImport().discogsArtistId(),
                response.artistDiscographyImport().status().name());

        return ResponseEntity.status(response.created() ? 201 : 200).body(responseDTO);
    }

    @Operation(summary = "Get artist's discography, supporting pagination and sort by year")
    @GetMapping("/{discogsArtistId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist's discography", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page  .class))),
            @ApiResponse(responseCode = "422", description = "Error getting Artist's discography", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    public ResponseEntity<Object> findDiscographyByArtistIdSortByYear(
            @PathVariable Long discogsArtistId,
            @RequestParam(defaultValue = "0" , name = "page") int page,
            @RequestParam(defaultValue = "50",name = "page_size") int size,
            @RequestParam(defaultValue = "asc", name = "sort_order") String sortDirection) {

        GetDiscographyByDiscogsArtistIdCommand command = new GetDiscographyByDiscogsArtistIdCommand(discogsArtistId, page, size, sortDirection);
        GetArtistDiscographyResponse response = artistDiscographyUseCase.findDiscographyByArtistIdSortByYear(command);
        if(response.error()) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(422, "Error getting Artist's discography", response.message(), java.time.LocalDateTime.now());
            return ResponseEntity.status(errorResponse.code()).body(errorResponse);
        }
        return ResponseEntity.ok(response.page());
    }

}

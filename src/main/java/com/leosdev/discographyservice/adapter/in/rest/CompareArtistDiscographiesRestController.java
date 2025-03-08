package com.leosdev.discographyservice.adapter.in.rest;

import com.leosdev.discographyservice.application.port.in.model.CompareArtistDiscographiesUseCase;
import com.leosdev.discographyservice.application.port.in.model.CompareDiscographiesByDiscogsAristIdCommand;
import com.leosdev.discographyservice.application.port.in.model.CompareDiscographiesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;


@RestController
@RequestMapping("v1/artists/discography")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),

})
class CompareArtistDiscographiesRestController {

    private final CompareArtistDiscographiesUseCase  compareArtistDiscographiesUseCase;


    public CompareArtistDiscographiesRestController(CompareArtistDiscographiesUseCase compareArtistDiscographiesUseCase ) {

        this.compareArtistDiscographiesUseCase=compareArtistDiscographiesUseCase;
    }


    @Operation(summary = "Import an artist from  Discogs API")
    @PostMapping("/compare")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist's discography already created", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    [{"discogsArtistId":123,"numReleases":207,"firstReleaseYear":1971,"lastReleaseYear":2023,"activeYears":52,"genres":["Balls","Rock"],"styles":["Bachata","Vallenato"]},{"discogsArtistId":456,"numReleases":207,"firstReleaseYear":1971,"lastReleaseYear":2023,"activeYears":53,"genres":["Blues","Rock"],"styles":["Bachata","Merengue"]}]
                    """))),
            @ApiResponse(responseCode = "422", description = "Error comparing discographies", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<Object> compareArtistDiscographies(@RequestBody CompareArtistDiscographiesRequestDTO  compareArtistDiscographiesRequestDTO) {
        CompareDiscographiesByDiscogsAristIdCommand command =  new CompareDiscographiesByDiscogsAristIdCommand( new HashSet<>(compareArtistDiscographiesRequestDTO.discogsArtistIds()));
        CompareDiscographiesResponse response = compareArtistDiscographiesUseCase.compareDicographiesByDiscogsArtistId(command);

        if (response.error()) {
            ErrorResponseDTO errorResponse = new ErrorResponseDTO(422, "Error comparing discographies", response.message(), java.time.LocalDateTime.now());
            return ResponseEntity.status(422)
                                 .body(errorResponse);
        }

        return ResponseEntity.status(200).body(response.artistDiscographySummaryList());
    }

}

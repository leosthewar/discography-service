package com.clara.discographyservice.adapter.in.rest;

import com.clara.discographyservice.application.port.in.SearchArtistFromDiscogsUseCase;
import com.clara.discographyservice.application.port.in.model.ArtistSearchQuery;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("v1/discogs")
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "502", description = "Error in the Discogs API", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
})
class DiscogsRestController {

    private final SearchArtistFromDiscogsUseCase searchArtistDiscogsUseCase;

    public DiscogsRestController(SearchArtistFromDiscogsUseCase searchArtistDiscogsUseCase) {
        this.searchArtistDiscogsUseCase = searchArtistDiscogsUseCase;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search result", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                    {"pagination":{"page":8,"pages":9,"per_page":50,"items":404,"urls":{"first":"http://domain.com/v1/discogs/artists/search?q=Nirvana&page=1&per_page=50","last":"http://domain.com/v1/discogs/artists/search?q=Nirvana&page=9&per_page=50","prev":"http://domain.com/v1/discogs/artists/search?q=Nirvana&page=7&per_page=50","next":"http://domain.com/v1/discogs/artists/search?q=Nirvana&page=9&per_page=50"}},"results":[{"id":125246,"type":"artist","master_id":null,"master_url":null,"uri":"/artist/125246-Nirvana","title":"Nirvana","thumb":"https://i.discogs.com/KydDnAWdAzeHy0dZ4YSnpkuh__uLXIk8w60uKQVW0G4/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","cover_image":"https://i.discogs.com/S_HB3FZR5TTcRyjBqlnUQpF_WgF-i9iSqcMTQGdwB6M/rs:fit/g:sm/q:90/h:609/w:600/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9BLTEyNTI0/Ni0xNTAxMjg1MjAw/LTMwNTguanBlZw.jpeg","resource_url":"https://api.discogs.com/artists/125246"}]}
                    """))),
    })
    @Operation(summary = "Search artists using Discogs API")
    @GetMapping("/artists/search")
    public ResponseEntity<JsonNode> searchArtist(@RequestParam(value = "q")  String query,
                                          @RequestParam(value = "page",required = false)  Integer page,
                                          @RequestParam(value = "per_page",required = false) Integer perPage){

        String currentURI = ServletUriComponentsBuilder.fromCurrentRequest()
                                                       .replaceQuery(null)
                                                       .build()
                                                       .toUriString();
        JsonNode response = searchArtistDiscogsUseCase.searchArtist(new ArtistSearchQuery(query, currentURI,page, perPage));
        return ResponseEntity.ok(response);
    }

}

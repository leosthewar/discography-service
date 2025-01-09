package com.clara.discographyservice.application.port.in.model;

import com.clara.discographyservice.application.domain.model.artist.responsemodel.ArtistDiscographySummaryResModel;

import java.util.List;

public record CompareDiscographiesResponse(List<ArtistDiscographySummaryResModel> artistDiscographySummaryList,
                                           String message,
                                           boolean error) {


}

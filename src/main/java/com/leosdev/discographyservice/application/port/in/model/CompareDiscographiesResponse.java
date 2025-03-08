package com.leosdev.discographyservice.application.port.in.model;

import com.leosdev.discographyservice.application.domain.model.artist.responsemodel.ArtistDiscographySummaryResModel;

import java.util.List;

public record CompareDiscographiesResponse(List<ArtistDiscographySummaryResModel> artistDiscographySummaryList,
                                           String message,
                                           boolean error) {


}

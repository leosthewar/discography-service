package com.clara.discographyservice.application.domain.model.release.responsemodel;

import java.util.List;

public record ReleaseResModel(Long id, Long discogsId, String status, Integer year, String discogsResourceUrl,
                              String discogsUri, Integer formatQuantity, String title, String country, String released,
                              String notes, String discogsThumb, Integer estimatedWeight, Boolean blockedFromSale,
                              List<ReleaseArtistResModel> artists, List<ReleaseLabelResModel> labels, List<ReleaseFormatResModel> formats,
                              List<ReleaseGenreResModel> genres, List<ReleaseStyleResModel> styles, List<ReleaseTrackResModel> tracks,
                              List<ReleaseImageResModel> images) {

}

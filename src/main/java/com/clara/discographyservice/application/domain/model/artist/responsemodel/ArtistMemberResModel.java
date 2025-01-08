package com.clara.discographyservice.application.domain.model.artist.responsemodel;


public record ArtistMemberResModel(Long id, Long discogsArtistId, String name, String discogsResourceUrl,
                                   Boolean active, String thumbnailUrl) {

}
